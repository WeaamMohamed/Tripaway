package com.example.tripaway.models;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpcomingTripModel {

    private String tripName;
    private String startPoint;
    private  String endPoint;

    //TODO: String date? String time?
    private String date;
    private String time;
    private boolean isOneDirection;
    private int repeat;
    private List<String> notes;

    public UpcomingTripModel(String tripName, String date, String time, List<String> notes) {
        this.tripName = tripName;
        this.date = date;
        this.time = time;
        this.notes = notes;
    }
    public UpcomingTripModel(String tripName, String startPoint, String endPoint,
                             String date, String time, boolean isOneDirection, int repeat,
                             List<String> notes) {
        this.tripName = tripName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;
        this.time = time;
        this.isOneDirection = isOneDirection;
        this.repeat = repeat;
        this.notes = notes;
    }



    public  Map<String, Object> getUpcomingTripsMap()
    {

        // Create a new user with map
        Map<String, Object> upcomingMap = new HashMap<>();
        upcomingMap.put("tripName", tripName);
        upcomingMap.put("startPoint", startPoint);
        upcomingMap.put("endPoint", endPoint);
        upcomingMap.put("date", date);
        upcomingMap.put("time", time);
        //TODO:
        //repeat.ordinal() to convert
        upcomingMap.put("repeat", repeat);
        upcomingMap.put("notes", notes);
        upcomingMap.put("isOneDirection", isOneDirection);



        return upcomingMap;

    }



    public enum Repeat{

        NO_REPEAT,
        DAILY,
        WEAKLY,
        MONTHLY;

        public static String fromIntegerToString(int x) {
            switch(x) {
                case 0:
                    return "NO_REPEAT";
                case 1:
                    return "DAILY";
                case 3:
                    return "WEAKLY";
                case 4:
                    return "MONTHLY";
            }
            return null;
        }

        public static Repeat fromIntegerToRepeatedValue(int x) {
            switch(x) {
                case 0:
                    return NO_REPEAT;
                case 1:
                    return DAILY;
                case 3:
                    return WEAKLY;
                case 4:
                    return MONTHLY;
            }
            return null;
        }


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

    public boolean isOneDirection() {
        return isOneDirection;
    }

    public void setOneDirection(boolean oneDirection) {
        this.isOneDirection = isOneDirection;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

}