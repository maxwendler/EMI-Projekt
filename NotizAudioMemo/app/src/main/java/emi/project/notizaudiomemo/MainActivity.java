package emi.project.notizaudiomemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Max on 15.12.2016.
 */

public class MainActivity extends AppCompatActivity implements NoteTypeDialogFragment.NoticeDialogListener {

    private FloatingActionButton btNewNote;
    private Button btClear;
    private DrawerLayout drawer; //der Drawer an sich
    private ListView drawerList; //die Liste der Items darin
    private ListView noteListView;

    //Folgende Variable zählt die Anzahl der Notizen, so dass jede neue Datei automatisch (lastId+1)
    //als ID erhalten kann, damit sie adäquat gespeichert werden kann. Für Typ seperat
    private int lastNoteId,lastAudioId;

    private File mainDataTxt;
    private NoteArray noteList;
    private ArrayAdapter<String> noteListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntializeActivity();
    }

    //ACHTUNG! Findet immer 2 mal statt, wenn mit Activity mit Intent über Back-Button aufgerufen
    @Override
    protected void onResume() {
        super.onResume();

        drawer.closeDrawers();  //Main startet & resumed mit geschlossenem Drawer

        Intent receivedIntent = getIntent();             //Notiz"titel" vom Editor empfangen

        try {
            if (receivedIntent.getType().equals("text")) {       //Neue Notiz
                createNoteListItem(receivedIntent.getStringExtra("title"),"text");
                receivedIntent.setType(null);
            }
            else if(receivedIntent.getType().equals("changed text")){    //Bearbeitete Notiz
                editNoteListItem(receivedIntent.getStringExtra("title"),
                                   receivedIntent.getStringExtra("id"));
                receivedIntent.setType(null);
            }
            else if(receivedIntent.getType().equals("audio")){           //Audio
                //Dialogfenster mit Eingabe des Titels
                int id= lastAudioId+1;

                Intent startAudioTitleActivity = new Intent(this,AudioTitleActivity.class);
                startAudioTitleActivity.putExtra("id","audio"+id);
                startActivity(startAudioTitleActivity);

                receivedIntent.setType(null);

            }else if (receivedIntent.getType().equals("audio title")){
                createNoteListItem(receivedIntent.getStringExtra("title"),"audio");
                receivedIntent.setType(null);
            }
        } catch (NullPointerException e) {
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        save();
    }

    @Override                   //um Intents auch bei on onResume verwenden zu können
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            setIntent(intent);
        }
    }

     private void IntializeActivity() {

        //Linking
        //Layout und Java-Code


        btNewNote = (FloatingActionButton) findViewById(R.id.FABnewNote);
        btClear = (Button) findViewById(R.id.buttonClear);

        drawerList = (ListView) findViewById(R.id.left_drawer);
        noteListView = (ListView) findViewById(R.id.ListViewNotes);

        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);


        //Intialisierung
        //


        noteList = new NoteArray();
        noteListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noteList.getTitles());

        //Datei wird geladen, wenn sie existiert
        mainDataTxt = new File(getFilesDir(), "mainData.txt");
        if (mainDataTxt.exists()) {
            load();
        } else {
            lastNoteId = 0;
            lastAudioId = 0;
        }

        //Drawer mit Items füllen
        ArrayList drawerItems = new ArrayList();
        drawerItems.add("Notizen");
        drawerItems.add("Papierkorb");
        drawerItems.add("Einstellungen");
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerItems));


        //Widgetkonfiguaration
        //

        btNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNote();
            }
        });

        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeActivity(position);
            }
        });

        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openNote(parent.getItemAtPosition(position).toString());
            }
        });
    }

    //AlertDialog-Fenster zur Auswahl der zu erstellenden Notiz
    private void createNote() {

        DialogFragment chooseTypeAlert = new NoteTypeDialogFragment();
        chooseTypeAlert.show(getSupportFragmentManager(),"NoteTypeDialogFragment");

    }

    @Override               //Im AlertDialog "TEXT" ausgwählt
    public void onDialogNegativeClick(DialogFragment dialog) {

        Intent openNoteEditor = new Intent(this, NoteEditorActivity.class);
        int id=lastNoteId+1;
        openNoteEditor.putExtra("id", "note"+id);
        startActivity(openNoteEditor);
    }

    @Override               //Im AlterDialog "AUDIO" ausgewählt
    public void onDialogPositiveClick(DialogFragment dialog) {

        Intent openAudioRecorder = new Intent(this, AudioRecorderActivity.class);
        int id=lastAudioId+1;
        openAudioRecorder.putExtra("id","audio"+id);
        startActivity(openAudioRecorder);

    }

    //Öffnet den Editor und sendet die ID der gewählten Notiz, damit Editor diese Laden kann
    //Der Type zeigt dem Editor an, dass er laden muss
    private void openNote(String title){

        String id=noteList.getIdByTitle(title);
        String type=noteList.getTypeByTitle(title);

        if (type=="text") {
            Intent openNoteInEditor = new Intent(this, NoteEditorActivity.class);
            openNoteInEditor.setType("activator");
            openNoteInEditor.putExtra("id", id);
            startActivity(openNoteInEditor);
        }

        if (type=="audio"){
            Intent openAudioPlayer = new Intent(this, AudioPlayerActivity.class);
            openAudioPlayer.putExtra("id",id);
            startActivity(openAudioPlayer);
        }

    }

    //Löscht alle Notizen
    //später ersetzen durch Reset-Knopf in Einstellungen
    private void clear() {
        noteList.clear();
        updateNoteListView();
        lastNoteId = 0;
        lastAudioId = 0;

        File notesDir = new File(getFilesDir(),"notes");
        if (notesDir.exists()){
            notesDir.delete();
        }
    }

    //Starten der Activities über den Drawer
    //Papierkorb und Settings schließen sich beim Öffnen einer anderen Activity selbst
    //Damit haben wir lediglich immer die Main laufen.
    private void changeActivity(int position) {
        if (position != 0) {
            switch (position) {
                case 1:
                    Intent openRecycleBin = new Intent(this, RecycleBinActivity.class);
                    startActivity(openRecycleBin);
                    break;
                case 2:
                    Intent openSettings = new Intent(this, SettingsActivity.class);
                    startActivity(openSettings);

            }
        }
    }

    //erstellt ein Item in der ListView entsprechend der gerade erstellten Notiz
    private void createNoteListItem(String title, String type) {
        String id="";

        if (type=="text"){
            lastNoteId++;
            id="note"+lastNoteId;
        }
        else if (type=="audio"){
            lastAudioId++;
            id="audio"+lastAudioId;
        }


        if(Arrays.asList(noteList.getTitles()).contains(title)){                //Damit Titel eindeutig sind
            title=title+" ";                                                    //Aber keinen Einfluss auf Inhalt der Notiz
        }

        noteList.add(id, title,type);

        updateNoteListView();        //aktualisiert Liste

    }

    private void editNoteListItem(String title,String id){
        noteList.edit(title,id);

        updateNoteListView();
    }

    //Speichert Datei mit allen bei Neustart relevanten Daten (lastIds und Titel der Notizen)
    private void save() {
        String data, noteTitleAndTypeString = "";

        try {
            if (mainDataTxt.exists()){mainDataTxt.delete();}
            mainDataTxt.createNewFile();
        } catch (IOException e) {
        }

        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(mainDataTxt);
        } catch (FileNotFoundException e) {
        }

        for (int i = noteList.getTitles().length-1; i >= 0; i--) {
            noteTitleAndTypeString = noteTitleAndTypeString + noteList.getTitles()[i] + ","+
                                     noteList.getTypes()[i]+"\n";
        }



        data =  "itemCount\n"+
                noteList.getTitles().length+"\n"+
                "notes\n" +
                noteTitleAndTypeString;


        try {
            writer.write(data.getBytes());
        } catch (IOException e) {
        }

    }

    //lädt Datei mit allen bei Neustart relevanten Daten (lastIds und Titel der Notizen)
    private void load() {
        String readLine = "";
        lastAudioId=0;lastNoteId=0;
        int itemCount=0;

        InputStream LoadS = null;
        try {
            LoadS = new FileInputStream(getFilesDir() + "/mainData.txt");
        } catch (FileNotFoundException e) {
        }

        Reader LoadSR = new InputStreamReader(LoadS);
        BufferedReader LoadBR = new BufferedReader(LoadSR);

        try {                                                            //Auslesen
            while ((readLine = LoadBR.readLine()) != null) {

                if(readLine.contains("itemCount")){
                    itemCount=Integer.valueOf(LoadBR.readLine());
                }

                if (readLine.contains("notes")) {
                    //Notizliste auslesen
                    for (int i = 0; i < itemCount; i++) {
                        String title="", type="", character;
                        int j=0;

                        readLine=LoadBR.readLine();
                        while (!(character=String.valueOf(readLine.charAt(j))).equals(",")){
                            title=title+character;
                            j++;
                        }

                        j++;

                        if (String.valueOf(readLine.charAt(j)).equals("t")){
                            type="text";
                            lastNoteId++;
                            noteList.add("note"+lastNoteId,title,type);
                        } else {
                            type="audio";
                            lastAudioId++;
                            noteList.add("audio"+lastAudioId,title,type);
                        }

                    }
                }
            }
        } catch (IOException e) {
        }

        updateNoteListView();
    }

    //aktualisiert Notizliste
    private void updateNoteListView() {
        noteListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noteList.getTitles());
        noteListView.setAdapter(noteListViewAdapter);
    }

}

