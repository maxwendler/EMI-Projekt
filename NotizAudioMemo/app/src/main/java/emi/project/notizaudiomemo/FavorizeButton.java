package emi.project.notizaudiomemo;

import android.content.Context;
import android.view.View;
import android.widget.Button;

/**
 * Created by Max on 16.01.2017.
 */

public class FavorizeButton extends Button {
    private final Context context;

    public FavorizeButton(Context context){
        super(context);
        this.context=context;
    }

    private boolean favorized=false;

    public void onClick (View v){
        if (!favorized){favorized=true;}
        else {favorized=false;}
    }

    public boolean isFavorized(){
        return favorized;
    }


}
