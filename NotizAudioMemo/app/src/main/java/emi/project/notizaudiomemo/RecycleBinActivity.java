package emi.project.notizaudiomemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Max on 15.12.2016.
 */

public class RecycleBinActivity extends AppCompatActivity{

    private ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_bin);

        IntializeActivity();
    }

    private void IntializeActivity(){

        drawerList= (ListView) findViewById(R.id.left_drawer);

        ArrayList drawerItems = new ArrayList();
        drawerItems.add("Notizen");drawerItems.add("Papierkorb");drawerItems.add("Einstellungen");
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerItems));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeActivity(position);
            }
        });
    }

    private void changeActivity(int position){
        if (position!=1){
            switch (position){
                case 0:
                    Intent backToMain = new Intent(this,MainActivity.class);
                    backToMain.setFlags(backToMain.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(backToMain);
                    break;
                case 2:
                    Intent openSettings = new Intent(this,SettingsActivity.class);
                    startActivity(openSettings);
                    finish();

            }
        }
    }
}
