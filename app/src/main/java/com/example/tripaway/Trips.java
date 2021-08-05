package com.example.tripaway;

public class Trips {
    String time;
    String date;
    String name;
    String startPoint;
    String endPoint;
    String[] notes;

    public Trips(String time, String date, String name, String startPoint, String endPoint, String[] notes) {
        this.time = time;
        this.date = date;
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.notes = notes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String[] getNotes() {
        return notes;
    }

    public void setNotes(String[] notes) {
        this.notes = notes;
    }
}
