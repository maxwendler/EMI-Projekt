package emi.project.notizaudiomemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

/**
 * Created by Max on 15.12.2016.
 */

public class MainActivity extends AppCompatActivity{

    private FloatingActionButton btNewNote;
    private DrawerLayout drawer; //der Drawer an sich
    private ListView drawerList; //die Liste der Items darin
    private ListView noteList;

    //Folgende Variable zählt die Anzahl der Notizen, so dass jede neue Notiz automatisch (noteNumber+1)
    //als ID erhalten kann, damit sie adäquat gespeichert werden kann. Muss später beim Start der App
    //geladen werden.
    private int noteNumber;

    private File mainDataTxt;
    private ArrayList noteTitles;
    private ArrayAdapter<String> noteListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntializeActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("Debug");

        drawer.closeDrawers();  //Main startet & resumed mit geschlossenem Drawer

        Intent receivedNoteTitle = getIntent();             //Notiz"titel" vom Editor empfangen

        try {                                               //bei Start ist der type null
            if (receivedNoteTitle.getType().equals("text")) {
                createNoteListItem(receivedNoteTitle.getStringExtra("title"));
            }
        }catch (NullPointerException e){}
    }

    @Override
    protected void onStop() {
        super.onStop();

        // save();
    }

    @Override                   //um Intents auch bei on onResume verwenden zu können
    protected void onNewIntent(Intent intent){
        if (intent!=null){
            setIntent(intent);
        }
    }

    private void  IntializeActivity(){

        //Linking
        //Layout und Java-Code


        btNewNote= (FloatingActionButton) findViewById(R.id.FABnewNote);

        drawerList= (ListView) findViewById(R.id.left_drawer);
        noteList= (ListView) findViewById(R.id.ListViewNotes);

        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);


        //Intialisierung
        //


        noteNumber=0;   //nur beim ersten Öffnen der App auf Gerät, später laden

        noteTitles = new ArrayList();
        noteListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,noteTitles);

        //Datei wird geladen, wenn sie existiert
        mainDataTxt = new File(getFilesDir(),"mainData.txt");
        if (mainDataTxt.exists()){
            load();
        }

        //Drawer mit Items füllen
        ArrayList drawerItems = new ArrayList();
        drawerItems.add("Notizen");drawerItems.add("Papierkorb");drawerItems.add("Einstellungen");
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerItems));


        //Widgetkonfiguaration
        //

        btNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNote();
            }
        });

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeActivity(position);
            }
        });
    }

    //Intent zum Öffnen des Notizeditors, schickt ID, damit Editor dann Notiz speichern kann
    private void createNote(){

        Intent openNoteEditor = new Intent(this, NoteEditorActivity.class);
        openNoteEditor.putExtra("ID",noteNumber);
        startActivity(openNoteEditor);

    }

    //Starten der Activities über den Drawer
    //Papierkorb und Settings schließen sich beim Öffnen einer anderen Activity selbst
    //Damit haben wir lediglich immer die Main laufen.
    private void changeActivity(int position){
        if (position!=0){
            switch (position){
                case 1:
                    Intent openRecycleBin = new Intent(this,RecycleBinActivity.class);
                    startActivity(openRecycleBin);
                    break;
                case 2:
                    Intent openSettings = new Intent(this,SettingsActivity.class);
                    startActivity(openSettings);

            }
        }
    }

    //erstellt ein Item in der ListView entsprechend der gerade erstellten Notiz
    private void createNoteListItem(String title){

        noteTitles.add(title);
        noteList.setAdapter(noteListAdapter);

        noteNumber++; //haben ja eine Notiz mehr
    }

    //Speichert Datei mit allen bei Neustart relevanten Daten
  /*  private void save(){
        String data;

        try{mainDataTxt.createNewFile();
        } catch (IOException e){}

        FileOutputStream writer = null;
        try {writer=new FileOutputStream(mainDataTxt);
        }catch (FileNotFoundException e){}

        data="noteNumber:\n"+noteNumber;

        try{writer.write(data.getBytes());
        } catch (IOException e) {}

    }   */

    //lädt Datei mit allen bei Neustart relevanten Daten
    private void load(){
        String readLine="";

        InputStream LoadS=null;
        try {LoadS = new FileInputStream(getFilesDir()+"/mainData.txt");
        }catch (FileNotFoundException e){}

        Reader LoadSR= new InputStreamReader(LoadS);
        BufferedReader LoadBR = new BufferedReader(LoadSR);

        try {                                                            //Auslesen
            while ((readLine = LoadBR.readLine()) != null) {
                if (readLine.contains("noteNumber")){
                    noteNumber=Integer.valueOf(LoadBR.readLine());
                }
            }
        }catch (IOException e) {}
    }
}
