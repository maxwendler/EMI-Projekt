package emi.project.notizaudiomemo;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
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


/**
 * Created by Max on 15.12.2016.
 */

public class NoteEditorActivity extends AppCompatActivity{

    private EditText tfContent;

    private int id;

    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        IntializeActivity();

    }

    private void IntializeActivity(){

        tfContent= (EditText) findViewById(R.id.editTextContent);

        Intent fromMain=getIntent();
        id = fromMain.getIntExtra("id", 0);
        state="new note";                                   //bleibt oder wird beim Laden verändert
        try {                                               //Laden falls durch Klick auf Notiz geöffnet
            if (fromMain.getType().equals("activator")) {
                load();
            }
        }catch (NullPointerException e){}
    }

    @Override
    protected  void onStop(){
        super.onStop();
        String content=tfContent.getText().toString();  //Müsstest du dann ändern, Fabian

        //"Titel" für Notizansicht senden

        boolean empty=true;

        //nichts speichern, wenn leer
        for(int i=0;i<content.length();i++){
            String x=String.valueOf(content.charAt(i));
            if (!x.equals(" ")){
                empty = false;
                //if(!(x+String.valueOf(content.charAt(i+1))).equals("\n")) { }           --> Dann wenn Leerzeilen auch ne Rolle spielen
            }
        }

        if (empty==false && state.equals("new note")) {                 //Speichern nur wenn nicht leer und noch nicht vorhanden
            Intent backtToMain = new Intent(this, MainActivity.class);
            backtToMain.setFlags(backtToMain.FLAG_ACTIVITY_CLEAR_TOP);
            backtToMain.putExtra("title", content);
            backtToMain.setType("text");

            saveNote(id,content);
            startActivity(backtToMain);
        }
    }


    //Darfst du so verändern wie du willst Fabian, ist unabhängig vom Rest
    private void saveNote(int id,String text){

        File notesDir= new File(getFilesDir(),"notes");                 //Ordner, wird einmalig erstellt
        if (!(notesDir.exists())){
                try {
                    notesDir.mkdir();
                }catch (SecurityException e){}
        }

        File noteTxt= new File(notesDir,String.valueOf(id)+".txt");     //Datei
        try{
            noteTxt.createNewFile();
        }catch (IOException e){}


        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(noteTxt);
        } catch (FileNotFoundException e) {
        }

        try {
            writer.write(text.getBytes());
        } catch (IOException e) {
        }
    }

    //Darfst du so verändern wie du willst Fabian, ist unabhängig vom Rest
    private void load(){
        state="loaded";

        InputStream LoadS = null;
        try {
            LoadS = new FileInputStream(getFilesDir()+"/notes/"+String.valueOf(id)+".txt");
        } catch (FileNotFoundException e) {System.out.println("File not found");
        }

        Reader LoadSR = new InputStreamReader(LoadS);
        BufferedReader LoadBR = new BufferedReader(LoadSR);

        try{
            tfContent.setText(LoadBR.readLine());
        } catch (IOException e){}
    }
}

