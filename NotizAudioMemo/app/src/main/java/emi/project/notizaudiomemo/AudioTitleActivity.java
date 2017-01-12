package emi.project.notizaudiomemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

/**
 * Created by Max on 12.01.2017.
 */

public class AudioTitleActivity extends FragmentActivity{

    private Button bCancel,bSave;
    private EditText eTitle;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_title);

        Intent fromMain = getIntent();
        id=fromMain.getStringExtra("id");

        IntializeActivity();
    }

    private void IntializeActivity(){
        bCancel = (Button) findViewById(R.id.button_cancel);
        bSave = (Button) findViewById(R.id.button_save);
        eTitle= (EditText) findViewById(R.id.editText_title);

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File audioFile = new File(getFilesDir()+"/notes/"+id+".3gpp");
                audioFile.delete();

                finish();
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

    }

    private void save (){
        Intent sendTitle = new Intent(this,MainActivity.class);
        sendTitle.putExtra("title",eTitle.getText().toString());
        sendTitle.setType("audio title");
        sendTitle.setFlags(sendTitle.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(sendTitle);
    }
}
