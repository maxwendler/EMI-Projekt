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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 15.12.2016.
 */

public class MainActivity extends AppCompatActivity{

    private FloatingActionButton btNewNote;
    private DrawerLayout drawer; //der Drawer an sich
    private ListView drawerList; //die Liste der Items darin

    //Folgende Variable zählt die Anzahl der Notizen, so dass jede neue Notiz automatisch (noteNumber+1)
    //als ID erhalten kann, damit sie adäquat gespeichert werden kann. Muss später beim Start der App
    //geladen werden.
    private int noteNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntializeActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();

        drawer.closeDrawers();  //Main startet & resumed mit geschlossenem Drawer
    }

    private void  IntializeActivity(){

        btNewNote= (FloatingActionButton) findViewById(R.id.FABnewNote);
        drawerList= (ListView) findViewById(R.id.left_drawer);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        noteNumber=0;   //nur beim ersten Öffnen der App auf Gerät, später laden

        //Drawer mit Items füllen
        ArrayList drawerItems = new ArrayList();
        drawerItems.add("Notizen");drawerItems.add("Papierkorb");drawerItems.add("Einstellungen");
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerItems));

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

        noteNumber++; //haben ja eine Notiz mehr

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

}
