package com.example.iav7;

public class JournalEntry implements java.io.Serializable{

    private String emotion;
    private String description;
    private String date;

    public JournalEntry(String e, String d1, String d2){
        emotion=e;
        description=d1;
        date=d2;
    }

    //getter and setter for variables
    public String getEmotion(){
        return emotion;
    }

    public void editEmotion(String e){
        e=emotion;
    }

    public String getDescription(){
        return description;
    }

    public void editDescription(String d){
        d1=description;
    }

    public String getDate(){
        return date;
    }

    public void editDate(String d){
        d2=date;
    }

    public String toString(){
        return "On " + getDate() + " your emotion was " + getEmotion() + " additionally you have commented: " +getDescription();
    }
}

