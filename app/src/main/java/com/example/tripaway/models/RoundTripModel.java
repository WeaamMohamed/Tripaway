package com.example.tripaway.models;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoundTripModel extends  UpcomingTripModel{

    private boolean isLocked;
    private String firstTripDocumentId;

    public RoundTripModel(){}

    public RoundTripModel(String tripName, String startPoint, String endPoint,
                          String date, String time, boolean isOneDirection, int repeat,
                          List<String> notes ,
                          Timestamp timestamp,
                          boolean isLocked, String firstTripDocumentId){

        this.isLocked = isLocked;
        this.firstTripDocumentId = firstTripDocumentId;
        setTripName(tripName);
        setStartPoint(startPoint);
        setEndPoint(endPoint);
        setDate(date);
        setTime(time);
        setOneDirection(isOneDirection);
        setRepeat(repeat);
        setNotes(notes);
        this.timestamp = timestamp;

    }

    public Map<String, Object> getRoundTripsMap()
    {

//        if(notes.isEmpty())
//        {
//            notes.add("You didn't have any note.");
//        }
        // Create a new user with map
        Map<String, Object> roundMap = new HashMap<>();
        roundMap.put("tripName", getTripName());
        roundMap.put("startPoint", getStartPoint());
        roundMap.put("endPoint", getEndPoint());
        //TODO:
        //repeat.ordinal() to convert
        roundMap.put("repeat", getRepeat());
        roundMap.put("date", getDate());
        roundMap.put("time", getTime());
        roundMap.put("notes", getNotes());
        roundMap.put("isOneDirection", isOneDirection());
        roundMap.put("timestamp", timestamp);
        roundMap.put("isLocked", isLocked);
        roundMap.put("firstTripDocumentId", firstTripDocumentId);



        return roundMap;

    }


    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String getFirstTripDocumentId() {
        return firstTripDocumentId;
    }

    public void setFirstTripDocumentId(String firstTripDocumentId) {
        this.firstTripDocumentId = firstTripDocumentId;
    }





}
