package edu.uncc.finalexam;
/*
File Name: NewsList.java
Full Name of author: Krithika Kasaragod
*/
import java.io.Serializable;

public class NewsList implements Serializable {
    String DID, UID, listItemName;
    int numberTrack = 0;

    public NewsList() {
    }

    public NewsList(String DID, String UID, String listItemName, int numberTrack) {
        this.DID = DID;
        this.UID = UID;
        this.listItemName = listItemName;
        this.numberTrack = numberTrack;
    }

    public String getDID() {
        return DID;
    }

    public void setDID(String DID) {
        this.DID = DID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getListItemName() {
        return listItemName;
    }

    public void setListItemName(String listItemName) {
        this.listItemName = listItemName;
    }

    public int getNumberTrack() {
        return numberTrack;
    }

    public void setNumberTrack(int numberTrack) {
        this.numberTrack = numberTrack;
    }

    @Override
    public String toString() {
        return "NewsList{" +
                "DID='" + DID + '\'' +
                ", UID='" + UID + '\'' +
                ", listItemName='" + listItemName + '\'' +
                ", numberTrack=" + numberTrack +
                '}';
    }
}
