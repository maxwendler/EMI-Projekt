package emi.project.notizaudiomemo;

/**
 * Created by Max on 19.12.2016.
 */

/**
 * 2 Arrays, die immer als verknüpft behandelt werden
 * Damit kann zu jeder Notiz eine ID gespeichert werden, was für das Speichern und Laden
 * der Notizen notwendig wird.
 */

public class NoteArray {
    private int [] ids;
    private String [] titles;

    public NoteArray(){
        ids=new int[0];
        titles= new String[0];
    }

    public int [] getIds(){
        return ids;
    }

    public String [] getTitles(){
        return titles;
    }


    //Könnte man evtl gebrauchen, können wir ja dann ggf implentieren
    /*public int getIdByTitle (String title){
        int index=0;

        if (!(titles.length==0)){
            //index = Position des Objektes
            return ids[index];

        } else {
            //Fehlermeldung
        }


    }

    public String getTitleById (int id){
        int index=0;

        if (!(titles.length==0)){
            //index = Position des Objektes
            return titles[index];

        } else {
            //Fehlermeldung
            return null;
        }

    }   */

    //Funktion getItem, die beides auf einmal returned (falls wir es brauchen)

    public void clear(){
        ids=new int[0];
        titles=new String[0];
    }

    //fügt neues Element am Index 0 hinzu, verschiebt den Rest nach hinten
    public void add(int id,String title){
        System.out.println("Debug");
        int[] a=ids;
        String[] b=titles;

        ids= new int[a.length+1];
        titles= new String[b.length+1];

        for (int i=0;i<a.length;i++){
            ids[i+1]=a[i];
            titles[i+1]=b[i];
        }

        ids[0]=id;
        titles[0]=title;
    }

    public void delete(){
        //darfst du machen, Minh :)
    }
}
