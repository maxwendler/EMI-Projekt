package emi.project.notizaudiomemo;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

/**
 * Created by Max on 07.01.2017.
 * Ansicht wie Dialogfenster durch FragmentActivity und theme siehe AndroidManifest
 */

public class AudioRecorderActivity extends FragmentActivity {

    private Button bSave,
                   bRecord;

    private String id;

    private MediaRecorder recorder;
    private boolean isRecording=false, isSaved=false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);

        IntializeActivity();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Damit Datei gelöscht wird, wenn man sie nicht speichern will
        if (!isSaved == true) {
            File memo = new File(getFilesDir() + "/notes/" + id + ".3gpp");
            if (memo.exists()) {
                memo.delete();
            }
        }
    }


    private void IntializeActivity(){
        bSave= (Button) findViewById(R.id.button_save);
        bRecord= (Button) findViewById(R.id.button_record);

        bSave.setEnabled(false);

        Intent fromMain=getIntent();
        id = fromMain.getStringExtra("id");

        bRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording==false){
                    bRecord.setText("STOP");
                    startRecording();
                }else if (isRecording==true){
                    bRecord.setText("AUFNAHME");
                    stopRecording();
                }
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMemo();
            }
        });
    }

    private void startRecording(){
        //Es wird untersucht, ob der Ordner vorhanden ist
        File notesDir = new File(getFilesDir(),"notes");
        if (!notesDir.exists()){
            try {
                notesDir.mkdir();
            }catch (SecurityException e){}
        }

        recorder= new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);         //Noch irgendwas wegen Permissions
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(getFilesDir()+"/notes/"+id+".3gpp");
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {}

        recorder.start();
        isRecording=true;
    }

    private void stopRecording(){
        recorder.stop();
        recorder.release();
        recorder = null;
        isRecording=false;

        bSave.setEnabled(true);
    }

    //Gespeichert lassen und in Liste der Main einfügen lassen
    private void saveMemo(){
        isSaved=true;

        //Dialogfeld für Titel fehlt noch

        Intent backtToMain = new Intent(this, MainActivity.class);
        backtToMain.setFlags(backtToMain.FLAG_ACTIVITY_CLEAR_TOP);
        backtToMain.putExtra("title", id);
        backtToMain.setType("audio");

        startActivity(backtToMain);
        }

}
