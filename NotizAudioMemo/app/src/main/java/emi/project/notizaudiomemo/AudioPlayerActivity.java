package emi.project.notizaudiomemo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
 * WORK IN PROGRESS!
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

    private void IntializeActivity(){
        bPlay= (Button) findViewById(R.id.button_play);
        pbPlayback = (ProgressBar) findViewById(R.id.progressBar_playback);

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

        try {
            player.prepare();
        }catch (IOException e){}


        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBarControl controller = null;

                String action= String.valueOf(bPlay.getText());
                if (action.equals("Play")){

                    player.start();

                    controller= new ProgressBarControl(player.getCurrentPosition());
                    controller.onStart();

                    bPlay.setText("Pause");
                }
                if (action.equals("Pause")){
                    player.pause();
                    controller=null;
                    bPlay.setText("Continue");
                }
                if (action.equals("Continue")){
                    player.start();
                    controller = new ProgressBarControl(player.getCurrentPosition());
                    controller.onStart();
                    bPlay.setText("Pause");
                }
            }
        });

        pbPlayback.setMax(player.getDuration());
    }

    class ProgressBarControl {
        private final ScheduledExecutorService scheduler= Executors.newScheduledThreadPool(1);
        int startPosition;

        public ProgressBarControl(int startPosition){
            this.startPosition=startPosition;
        }

        public void onStart(){
            final Runnable updateProgressBar= new Runnable() {
                @Override
                public void run() {
                    int progress=player.getCurrentPosition();
                    if (player.getCurrentPosition()==player.getDuration()){
                    }
                    pbPlayback.setProgress(progress);
                }
            };

            final ScheduledFuture<?> updateProgressBarHandle =
                    scheduler.scheduleAtFixedRate(updateProgressBar,0,10, TimeUnit.MILLISECONDS);

            final Runnable cancelUpdating = new Runnable() {
                @Override
                public void run() {
                    bPlay.setText("Play");
                    pbPlayback.setProgress(0);
                    updateProgressBarHandle.cancel(true);
                }
            };

            scheduler.schedule(cancelUpdating,player.getDuration()-startPosition,TimeUnit.MILLISECONDS);

        }
    }
}
