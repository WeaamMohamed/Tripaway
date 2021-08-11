package com.example.tripaway.models;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OldTripsModel {



    private boolean done;
    private String tripName;
    private String startPoint;
    private String endPoint;
    private String speed;
    private String distance;
    private List<String> notes;
    private String date;
    private String time;
    private Timestamp timestamp;



    public OldTripsModel(boolean done, String tripName, String startPoint,
                         String endPoint, String speed,
                         String distance, List<String> notes,
                         String date,
                         String time,
                         Timestamp timestamp) {
        this.done = done;
        this.tripName = tripName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.speed = speed;
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
        oldTripsMap.put("speed", speed);
        oldTripsMap.put("distance", distance);
        oldTripsMap.put("isDone", done);
        oldTripsMap.put("date", date);
        oldTripsMap.put("time", time);
        oldTripsMap.put("timestamp", timestamp);



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



    public String getSpeedInformation() {
        return speed;
    }

    public void setSpeedInformation(String speed) {
        this.speed = speed;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
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

    public String getDistanceInformation() {
        return distance;
    }

    public void setDistanceInformation(String distanceInformation) {
        this.distance = distanceInformation;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }
}