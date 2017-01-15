package emi.project.notizaudiomemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.jar.Manifest;

/**
 * Created by Max on 07.01.2017.
 * Ansicht wie Dialogfenster durch FragmentActivity und theme siehe AndroidManifest
 */

public class AudioRecorderActivity extends FragmentActivity {

    private Button bRecord;

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
        Intent backtToMain = new Intent(this, MainActivity.class);
        backtToMain.setFlags(backtToMain.FLAG_ACTIVITY_CLEAR_TOP);
        backtToMain.setType("audio");

        startActivity(backtToMain);
    }


    private void IntializeActivity(){
        bRecord= (Button) findViewById(R.id.button_record);

        Intent fromMain=getIntent();
        id = fromMain.getStringExtra("id");

        bRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording==false){
                    startRecording();
                }else if (isRecording==true){
                    stopRecording();
                }
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

        //Permission zur Aufnahme überprüfen und ggf. anfordern

        int permissionCheck= ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO);

        if (permissionCheck==PackageManager.PERMISSION_GRANTED) {
            startRecorder();
        }else{
            //Öffnet Dialogfenster um Permission anzufordern
            //Die Auswahl des Nutzers wird an onRequestPermissionsResult() weitergegeben
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO},1);

        }
    }

    private void startRecorder(){
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(getFilesDir() + "/notes/" + id + ".3gpp");
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
        }

        recorder.start();
        isRecording = true;

        bRecord.setText("STOP");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(requestCode==1){
            if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                startRecorder();
            }
        }
    }

    private void stopRecording(){
        recorder.stop();
        recorder.release();
        recorder = null;
        isRecording=false;

        finish();

    }
}
