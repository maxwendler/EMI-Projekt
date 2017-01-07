package emi.project.notizaudiomemo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.IOException;

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
        player.reset();

        try {
            player.setDataSource(getFilesDir() + "/notes/" + id + ".3ggp");
        }catch (IOException e){}


        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action= String.valueOf(bPlay.getText());
                if (action.equals("Play")){
                    try {
                        player.prepare();
                    }catch (IOException e){}
                    player.start();
                    bPlay.setText("Pause");
                }
                if (action.equals("Pause")){
                    player.pause();
                    bPlay.setText("Continue");
                }
                if (action.equals("Continue")){
                    player.start();
                }
            }
        });

        pbPlayback.setMax(player.getDuration());
    }
}
