package emi.project.notizaudiomemo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by Max on 16.01.2017.
 */

public class NoteListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private FavorizeClicked favorizeClicked;

    public NoteListArrayAdapter (Context context, String[] values, FavorizeClicked favorizeClicked){
        super(context,-1,values);
        this.context=context;
        this.values=values;
        this.favorizeClicked=favorizeClicked;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        LinearLayout horizontalLinearLayout = (LinearLayout) rowView.findViewById(R.id.LinearLayout1);
        int width= parent.getWidth();

        LinearLayout verticalLinearLayout =
                (LinearLayout) horizontalLinearLayout.findViewById(R.id.LinearLayout2);
        int vLLWidth=4*(width/5);
        //Log.e("getView",String.valueOf(width));
        //Log.e("getView",String.valueOf(vLLWidth));
        verticalLinearLayout.setLayoutParams(
                new LinearLayout.LayoutParams(vLLWidth,LinearLayout.LayoutParams.MATCH_PARENT));


        ToggleButton bFavorize = (ToggleButton) horizontalLinearLayout.findViewById(R.id.button_favorize);
        int bFavorizeSize=(width/5)-100;
        LinearLayout.LayoutParams bFvrzParams= new LinearLayout.LayoutParams(
                bFavorizeSize, bFavorizeSize);
        int marginLeftRight = (width/5-bFavorizeSize)/2;
        bFvrzParams.setMargins(marginLeftRight,20,marginLeftRight,20);
        bFavorize.setLayoutParams(bFvrzParams);

        bFavorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("FavorizeButton","Button clicked");
                favorizeClicked.moveNoteListItem(position);
            }
        });

        TextView tvTitle = (TextView) verticalLinearLayout.findViewById(R.id.textView_title);
        TextView tvType = (TextView) verticalLinearLayout.findViewById(R.id.textView_type);

        String item = values[position];

        if (item.startsWith("text")){
            tvType.setText("Text");
            String title="";
            for (int i=5;i<item.length();i++){
                title=title+item.charAt(i);
            }
            tvTitle.setText(title);
        } else if (item.startsWith("audio")){
            tvType.setText("Audio");
            String title="";
            for (int i=6;i<item.length();i++){
                title=title+item.charAt(i);
            }
            tvTitle.setText(title);
        } else {
            Log.e("LayoutInflater","invalid items");}



        return rowView;
    }

}
