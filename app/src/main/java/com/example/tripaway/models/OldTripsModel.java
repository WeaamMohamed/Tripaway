package com.example.tripaway.models;



import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OldTripsModel {



    private boolean isDone;
    private String tripName;
    private String startPoint;
    private String endPoint;
    private String speed;
    private String distance;
    private List<String> notes;

    private Timestamp timestamp;
    private String date;
    private String time;

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

    public  OldTripsModel(){

    }

    public OldTripsModel(boolean isDone, String tripName, String startPoint, String endPoint, String speed,
                         String distance,
                         List<String> notes,
                         String date, String time,
                         Timestamp timestamp) {
        this.isDone = isDone;
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



    public Map<String, Object> getOldTripsMap()
    {

        // Create a new user with map
        Map<String, Object> oldTripsMap = new HashMap<>();
        oldTripsMap.put("tripName", tripName);
        oldTripsMap.put("startPoint", startPoint);
        oldTripsMap.put("endPoint", endPoint);
        // oldTripsMap.put("Notes", notes);
        oldTripsMap.put("speedInformation", speed);
        oldTripsMap.put("distanceInformation", distance);
        oldTripsMap.put("done", isDone);
        oldTripsMap.put("timestamp", timestamp);




        return oldTripsMap;

    }




    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        this.isDone = done;
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

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }
}

//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class OldTripsModel {
//
//
//
//    private boolean done;
//    private String tripName;
//    private String startPoint;
//    private String endPoint;
//    private String speedInformation;
//    private String distanceInformation;
//    private List<String> notes;
//
//
//    public OldTripsModel(boolean done, String tripName, String startPoint,
//                         String endPoint, String speedInformation,
//                         String distanceInformation, List<String> notes) {
//        this.done = done;
//        this.tripName = tripName;
//        this.startPoint = startPoint;
//        this.endPoint = endPoint;
//        this.speedInformation = speedInformation;
//        this.distanceInformation = distanceInformation;
//        this.notes = notes;
//    }
//
//
//
//    public Map<String, Object> getOldTripsMap()
//    {
//
//        // Create a new user with map
//        Map<String, Object> oldTripsMap = new HashMap<>();
//        oldTripsMap.put("tripName", tripName);
//        oldTripsMap.put("startPoint", startPoint);
//        oldTripsMap.put("endPoint", endPoint);
//        oldTripsMap.put("Notes", notes);
//        oldTripsMap.put("speedInformation", speedInformation);
//        oldTripsMap.put("distanceInformation", distanceInformation);
//        oldTripsMap.put("done", done);
//
//
//
//        return oldTripsMap;
//
//    }
//
//
//
//
//    public boolean isDone() {
//        return done;
//    }
//
//    public void setDone(boolean done) {
//        this.done = done;
//    }
//
//    public String getTripName() {
//        return tripName;
//    }
//
//    public void setTripName(String tripName) {
//        this.tripName = tripName;
//    }
//
//    public String getStartPoint() {
//        return startPoint;
//    }
//
//    public void setStartPoint(String startPoint) {
//        this.startPoint = startPoint;
//    }
//
//    public String getEndPoint() {
//        return endPoint;
//    }
//
//    public void setEndPoint(String endPoint) {
//        this.endPoint = endPoint;
//    }
//
//
//
//    public String getSpeedInformation() {
//        return speedInformation;
//    }
//
//    public void setSpeedInformation(String speedInformation) {
//        this.speedInformation = speedInformation;
//    }
//
//    public String getDistanceInformation() {
//        return distanceInformation;
//    }
//
//    public void setDistanceInformation(String distanceInformation) {
//        this.distanceInformation = distanceInformation;
//    }
//
//    public List<String> getNotes() {
//        return notes;
//    }
//
//    public void setNotes(List<String> notes) {
//        this.notes = notes;
//    }
//}