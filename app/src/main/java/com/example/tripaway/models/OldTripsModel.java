package com.example.tripaway.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OldTripsModel {



    private boolean done;
    private String tripName;
    private String startPoint;
    private String endPoint;
    private String duration;
    private String distance;
    private List<String> notes;
    private String date;
    private String time;
    protected Timestamp timestamp;
    private boolean isOneDirection;



    //for round trip
    private List<String> tripNameList = null;
    private List<String> startPointList = new ArrayList<>();
    private  List<String> endPointList = new ArrayList<>();
    private List<String> dateList = new ArrayList<>();
    private List<String> timeList ;
    //private boolean isOneDirection;
    private int repeatList;
    private List<String> notesList = null;
    private int finishedTrips ;
    private int currentActiveTrip;


    public List<String> getTripNameList() {
        return tripNameList;
    }

    public void setTripNameList(List<String> tripNameList) {
        this.tripNameList = tripNameList;
    }

    public List<String> getStartPointList() {
        return startPointList;
    }

    public void setStartPointList(List<String> startPointList) {
        this.startPointList = startPointList;
    }

    public List<String> getEndPointList() {
        return endPointList;
    }

    public void setEndPointList(List<String> endPointList) {
        this.endPointList = endPointList;
    }

    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    public List<String> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<String> timeList) {
        this.timeList = timeList;
    }

    public int getRepeatList() {
        return repeatList;
    }

    public void setRepeatList(int repeatList) {
        this.repeatList = repeatList;
    }

    public List<String> getNotesList() {
        return notesList;
    }

    public void setNotesList(List<String> notesList) {
        this.notesList = notesList;
    }

    public int getFinishedTrips() {
        return finishedTrips;
    }

    public void setFinishedTrips(int finishedTrips) {
        this.finishedTrips = finishedTrips;
    }

    public int getCurrentActiveTrip() {
        return currentActiveTrip;
    }

    public void setCurrentActiveTrip(int currentActiveTrip) {
        this.currentActiveTrip = currentActiveTrip;
    }

//    public Timestamp getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(Timestamp timestamp) {
//        this.timestamp = timestamp;
//    }

    public boolean isOneDirection() {
        return isOneDirection;
    }

    public void setOneDirection(boolean oneDirection) {
        isOneDirection = oneDirection;
    }

    public OldTripsModel(boolean done, String tripName, String startPoint,
                         String endPoint, String duration,
                         String distance, List<String> notes,
                         String date,
                         String time,
                         Timestamp timestamp) {
        this.done = done;
        this.tripName = tripName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.duration = duration;
        this.distance = distance;
        this.notes = notes;
        this.date = date;
        this.time = time;
        this.timestamp = timestamp;
    }

    public OldTripsModel(){}


    public Map<String, Object> getOldTripsMap()
    {

        // Create a new user with map
        Map<String, Object> oldTripsMap = new HashMap<>();
        oldTripsMap.put("tripName", tripName);
        oldTripsMap.put("startPoint", startPoint);
        oldTripsMap.put("endPoint", endPoint);
        oldTripsMap.put("notes", notes);
        oldTripsMap.put("duration", duration);
        oldTripsMap.put("distance", distance);
        oldTripsMap.put("isDone", done);
        oldTripsMap.put("date", date);
        oldTripsMap.put("time", time);
        oldTripsMap.put("timestamp", timestamp);
        oldTripsMap.put("isOneDirection", isOneDirection);



        return oldTripsMap;

    }




    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }
}