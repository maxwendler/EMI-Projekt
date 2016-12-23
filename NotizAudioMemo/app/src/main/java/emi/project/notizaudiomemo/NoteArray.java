package emi.project.notizaudiomemo;

/**
 * Created by Max on 19.12.2016.
 */

import java.util.Arrays;

/**
 * 2 Arrays, die immer als verknüpft behandelt werden
 * Damit kann zu jeder Notiz eine ID gespeichert werden, was für das Speichern und Laden
 * der Notizen notwendig wird.
 */

public class NoteArray {
    private int [] ids;
    private String [] titles;
    private String [] types;

    public NoteArray(){
        ids=new int[0];
        titles= new String[0];
        types= new String[0];
    }

    public int [] getIds(){
        return ids;
    }

    public String [] getTitles(){
        return titles;
    }

    public String[] getTypes() {return types;}

    public int getIdByTitle (String title){
        int index=0;

        if (Arrays.asList(titles).contains(title)) {
            index = Arrays.asList(titles).indexOf(title);
            if (!(titles.length==0)) {
                return ids[index];
            } else {return -1;}
        } else {return -2;}
    }

    public String getTitleById (int id){
        int index=0;

        if (Arrays.asList(ids).contains(id)) {
            index = Arrays.asList(ids).indexOf(id);
            if (!(ids.length==0)) {
                return titles[index];
            } else {return null;}
        } else {return null;}
    }

    public String getTypeById (int id){
        int index=0;

        if (Arrays.asList(ids).contains(id)) {
            index = Arrays.asList(ids).indexOf(id);
            if (!(ids.length==0)) {
                return types[index];
            } else {return null;}
        } else {return null;}
    }

    public String getTypeByTitle (String title){
        int index=0;

        if (Arrays.asList(titles).contains(title)) {
            index = Arrays.asList(titles).indexOf(title);
            if (!(titles.length==0)) {
                return types[index];
            } else {return null;}
        } else {return null;}
    }

    //Funktion getItem, die beides auf einmal returned (falls wir es brauchen)

    public void clear(){
        ids=new int[0];
        titles=new String[0];
    }

    //fügt neues Element am Index 0 hinzu, verschiebt den Rest nach hinten
    public void add(int id,String title, String type){
        int[] a=ids;
        String[] b=titles;
        String[] c=types;

        ids= new int[a.length+1];
        titles= new String[b.length+1];
        types= new String[c.length+1];

        for (int i=0;i<a.length;i++){
            ids[i+1]=a[i];
            titles[i+1]=b[i];
            types[i+1]=c[i];
        }

        ids[0]=id;
        titles[0]=title;
        types[0]=type;
    }

    public void delete(){
        //darfst du machen, Minh :)
    }
}