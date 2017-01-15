package emi.project.notizaudiomemo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Max on 07.01.2017.
 * Ansicht wie Dialogfenster durch FragmentActivity und theme siehe AndroidManifest
 */

public class AudioPlayerActivity extends FragmentActivity {

    private Button bPlay;
    private ProgressBar pbPlayback;
    private String id;

    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        IntializeActivity();
    }

    @Override
    protected void onPause(){
        Log.e("AudioPlayerActivity","Pause");
        super.onPause();

        player.stop();
        player.release();

    }


    private void IntializeActivity(){
        bPlay= (Button) findViewById(R.id.button_play);
        pbPlayback = (ProgressBar) findViewById(R.id.progressBar_playback);

        bPlay.setEnabled(false);

        Intent fromMain=getIntent();
        id=fromMain.getStringExtra("id");

        player= new MediaPlayer();

        File source = new File(getFilesDir() + "/notes/" + id + ".3gpp");
        if (source.exists()){
            Log.e("File","File found");
        }else{Log.e("File","File not found");}


        try {
            player.setDataSource(source.getPath());
        }catch (IOException e){}

        player.prepareAsync();

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                bPlay.setEnabled(true);
            }
        });

        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBarControl controller = null;
                pbPlayback.setMax(player.getDuration());

                String action= String.valueOf(bPlay.getText());
                if (action.equals("Play")){
                    bPlay.setText("Pause");
                    controller= new ProgressBarControl(player.getCurrentPosition());
                    player.start();
                    controller.onStart();
                                    }
                if (action.equals("Pause")){
                    bPlay.setText("Continue");
                    controller=null;
                    player.pause();
                                    }
                if (action.equals("Continue")){
                    bPlay.setText("Pause");
                    controller = new ProgressBarControl(player.getCurrentPosition());
                    player.start();
                    controller.onStart();
                    }
            }
        });


    }
    //Um den Progress Bar immer wieder zu updaten
    class ProgressBarControl {
        private final ScheduledExecutorService scheduler= Executors.newScheduledThreadPool(1);
        int startPosition;

        public ProgressBarControl(int startPosition){
            this.startPosition=startPosition;
        }

        public void onStart(){
            //Das was ausgeführt wird
            final Runnable updateProgressBar= new Runnable() {
                @Override
                public void run() {
                    int progress=player.getCurrentPosition();
                    pbPlayback.setProgress(progress);
                    if (progress==pbPlayback.getMax()){
                        bPlay.setText("Play");
                        throw  new RuntimeException();  //Abbruch
                    }
                }
            };
            //Befehl zum immer wieder ausführen
            final ScheduledFuture<?> updateProgressBarHandle =
                    scheduler.scheduleAtFixedRate(updateProgressBar,0,10, TimeUnit.MILLISECONDS);

        }
    }
}
