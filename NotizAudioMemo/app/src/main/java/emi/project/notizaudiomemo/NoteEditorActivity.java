package emi.project.notizaudiomemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Max on 15.12.2016.
 */

public class NoteEditorActivity extends AppCompatActivity{

    private EditText tfContent;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        IntializeActivity();

        Intent receiveId=getIntent();
        id=receiveId.getIntExtra("id",0);
    }

    @Override
    protected  void onStop(){
        super.onStop();
        String content=tfContent.getText().toString();

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

        if (empty==false) {
            Intent backtToMain = new Intent(this, MainActivity.class);
            backtToMain.setFlags(backtToMain.FLAG_ACTIVITY_CLEAR_TOP);
            backtToMain.putExtra("title", content);
            backtToMain.setType("text");

            // saveNote(id,content);               --> funktioniert noch nicht

            startActivity(backtToMain);
        }
    }

    private void IntializeActivity(){

        tfContent= (EditText) findViewById(R.id.editTextContent);

    }

    //muss noch gedebuggt werden
    private void saveNote(int id,String text){

        File noteFile= new File(getFilesDir(),"Notes");
        if (!(noteFile.exists())){
                try {
                    noteFile.createNewFile();
                }catch (IOException e){}
        }

        File noteTxt= new File(noteFile,String.valueOf(id)+".txt");
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

}

