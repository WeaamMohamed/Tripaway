package com.example.tripaway.models;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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
    protected Timestamp timestamp;




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

//    public boolean isOneDirectionList() {
//        return isOneDirectionList;
//    }
//
//    public void setOneDirectionList(boolean oneDirectionList) {
//        isOneDirectionList = oneDirectionList;
//    }

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

    public ArrayList<UpcomingTripModel> getRoundTrip() {
        return roundTrip;
    }

    public void setRoundTrip(ArrayList<UpcomingTripModel> roundTrip) {
        this.roundTrip = roundTrip;
    }

    ArrayList<UpcomingTripModel> roundTrip;
   // private Map<String , Object> map;


    public  UpcomingTripModel(){}

    public UpcomingTripModel(String tripName, String date, String time) {
        this.tripName = tripName;
        this.date = date;
        this.time = time;
    }
    public UpcomingTripModel(String tripName, String startPoint, String endPoint,
                             String date, String time, boolean isOneDirection, int repeat,
                             List<String> notes ,
                             Timestamp timestamp) {
        this.tripName = tripName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;
        this.time = time;
        this.isOneDirection = isOneDirection;
        this.repeat = repeat;
        this.notes = notes;
        this.timestamp = timestamp;
    }

    public UpcomingTripModel(List<String> tripNameList,List<String> startPointList,
                             List<String> endPointList,
                             List<String> dateList,
                             List<String> timeList,
                             boolean isOneDirection,
                              int repeatList,
                             List<String> notesList,

                             int finishedTrips,
                             int currentActiveTrip,
                             Timestamp timestamp

                             ){

        roundTrip = new ArrayList<>();
        this.tripNameList =  tripNameList;
        this.startPointList =  startPointList;
        this.endPointList =  endPointList;
        this.dateList =  dateList;
        this.timeList =  timeList;
        this.isOneDirection =  isOneDirection;
        this.repeatList =  repeatList;
        this.notesList =  notesList;
        this.finishedTrips =  finishedTrips;
        this.currentActiveTrip = currentActiveTrip;
        this.timestamp = timestamp;





    }

//    public UpcomingTripModel(Map<String, Object> map){
//        this.map = map;

//    }

    public  Map<String, Object> getUpcomingTripsMap()
    {

//        if(notes.isEmpty())
//        {
//            notes.add("You didn't have any note.");
//        }
        // Create a new user with map

        Map<String, Object> upcomingMap = new HashMap<>();

        upcomingMap.put("tripName", tripName);
        upcomingMap.put("startPoint", startPoint);
        upcomingMap.put("endPoint", endPoint);
        //TODO:
        //repeat.ordinal() to convert
        upcomingMap.put("repeat", repeat);
        upcomingMap.put("date", date);
        upcomingMap.put("time", time);
        upcomingMap.put("notes", notes);
        upcomingMap.put("isOneDirection", isOneDirection);
        upcomingMap.put("timestamp", timestamp);
        upcomingMap.put("finishedTrips", 0);




        return upcomingMap;

    }



    public  Map<String, Object> getRoundTripsMap()
    {

//        if(notes.isEmpty())
//        {
//            notes.add("You didn't have any note.");
//        }
        // Create a new user with map



//        roundTrip.add(new UpcomingTripModel("first roundtrip", "start", "end" ,
//                "Friday, Aug 20, 2021","5:24", false, 1, null,
//                new Timestamp(System.currentTimeMillis())));
//
// roundTrip.add(new UpcomingTripModel("second round", "start", "end" ,
//                "Friday, Aug 20, 2021","5:24", false, 1, null,
//                new Timestamp(System.currentTimeMillis())));


        Map<String, Object> roundTripMap = new HashMap<>();

//        roundTripMap.put("tripNameList", Arrays.asList(roundTrip.get(0).tripName, roundTrip.get(1).tripName));
//        roundTripMap.put("startPointList",  Arrays.asList(roundTrip.get(0).startPoint, roundTrip.get(1).startPoint));
//        roundTripMap.put("endPointList",  Arrays.asList(roundTrip.get(0).endPoint, roundTrip.get(1).endPoint));
//        //TODO:
//        //repeat.ordinal() to convert
//        roundTripMap.put("repeatList",  Arrays.asList(roundTrip.get(0).repeat, roundTrip.get(1).repeat));
//        roundTripMap.put("dateList",  Arrays.asList(roundTrip.get(0).date, roundTrip.get(1).date));
//        roundTripMap.put("timeList",  Arrays.asList(roundTrip.get(0).time, roundTrip.get(1).time));
//        roundTripMap.put("notesList",  Arrays.asList(roundTrip.get(0).notes, roundTrip.get(1).notes));
//        roundTripMap.put("isOneDirectionList",  Arrays.asList(roundTrip.get(0).isOneDirection, roundTrip.get(1).isOneDirection));
//        roundTripMap.put("timestampList",  Arrays.asList(roundTrip.get(0).timestamp, roundTrip.get(1).timestamp));
//        roundTripMap.put("isFinished",  false);

        roundTripMap.put("tripNameList", tripNameList);
        roundTripMap.put("startPointList", startPointList);
        roundTripMap.put("endPointList",  endPointList);
        //TODO:
        //repeat.ordinal() to convert
        roundTripMap.put("repeatList", repeatList);
        roundTripMap.put("dateList", dateList);
        roundTripMap.put("timeList",  timeList);
        roundTripMap.put("notesList",  notesList);
        roundTripMap.put("isOneDirection",  isOneDirection);
        roundTripMap.put("timestamp", timestamp);
        roundTripMap.put("finishedTrips",  finishedTrips);
        roundTripMap.put("currentActiveTrip",  currentActiveTrip);




        return roundTripMap;


    }

//    public void fromMap()
//    {
//
//        this.tripName = (String) map.get("tripName");
//        this.startPoint = (String) map.get("startPoint");
//        this.endPoint = (String) map.get("endPoint");
//        this.date = (String) map.get("date");
//        this.time = (String) map.get("time");
//        this.isOneDirection = (boolean) map.get("isOneDirection");
//        this.repeat = (int) map.get("repeat");
//        this.timestamp = (Timestamp) map.get("timestamp");
//        this.notes = (List<String>) map.get("notes");
//
//
//    }



    public enum Repeat{

        NO_REPEAT,
        DAILY,
        WEAKLY,
        MONTHLY,

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
        this.isOneDirection = oneDirection;
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


    public int finishedTrips() {
        return finishedTrips;
    }

    public void setFinished(int finishedTrips) {
        this.finishedTrips = finishedTrips;
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
}