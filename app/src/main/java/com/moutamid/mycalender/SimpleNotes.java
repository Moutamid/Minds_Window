package com.moutamid.mycalender;

import com.google.firebase.database.Exclude;

public class SimpleNotes {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    

    @Exclude
    String id;
    String note;
    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
    Long date;

    public String getUid() {
        return uid;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    Boolean status;

    public void setUid(String uid) {
        this.uid = uid;
    }

    String uid;

}
