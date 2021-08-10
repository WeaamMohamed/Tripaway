package com.example.tripaway.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OldTripsModel {



    private boolean done;
    private String tripName;
    private String startPoint;
    private String endPoint;
    private String speedInformation;
    private String distanceInformation;
    private List<String> notes;


    public OldTripsModel(boolean done, String tripName, String startPoint,
                         String endPoint, String speedInformation,
                         String distanceInformation, List<String> notes) {
        this.done = done;
        this.tripName = tripName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.speedInformation = speedInformation;
        this.distanceInformation = distanceInformation;
        this.notes = notes;
    }



    public Map<String, Object> getOldTripsMap()
    {

        // Create a new user with map
        Map<String, Object> oldTripsMap = new HashMap<>();
        oldTripsMap.put("tripName", tripName);
        oldTripsMap.put("startPoint", startPoint);
        oldTripsMap.put("endPoint", endPoint);
        oldTripsMap.put("Notes", notes);
        oldTripsMap.put("speedInformation", speedInformation);
        oldTripsMap.put("distanceInformation", distanceInformation);
        oldTripsMap.put("done", done);



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
        return speedInformation;
    }

    public void setSpeedInformation(String speedInformation) {
        this.speedInformation = speedInformation;
    }

    public String getDistanceInformation() {
        return distanceInformation;
    }

    public void setDistanceInformation(String distanceInformation) {
        this.distanceInformation = distanceInformation;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }
}