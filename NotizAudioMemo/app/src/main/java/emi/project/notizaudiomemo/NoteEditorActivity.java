package emi.project.notizaudiomemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;

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
    }

    @Override
    protected  void onStop(){
        super.onStop();
        String content=tfContent.getText().toString();

        //"Titel" für Notizansicht senden

        //wenn leer, wird auch nichts gespeichert; leer auch als Leerzeichen?!
        if (!(content.equals(""))) {
            Intent backtToMain = new Intent(this, MainActivity.class);
            backtToMain.setFlags(backtToMain.FLAG_ACTIVITY_CLEAR_TOP);
            backtToMain.putExtra("title", content);
            backtToMain.setType("text");

            saveNote(id,content);

            startActivity(backtToMain);
        }
    }

    private void IntializeActivity(){

        tfContent= (EditText) findViewById(R.id.editTextContent);

    }

    private void saveNote(int id,String text){

    }

}

