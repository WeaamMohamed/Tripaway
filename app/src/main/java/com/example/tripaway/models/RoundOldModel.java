package com.example.tripaway.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RoundOldModel extends OldTripsModel{


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


    public RoundOldModel(){}

    public RoundOldModel(List<String> tripNameList, List<String> startPointList, List<String> endPointList, List<String> dateList, List<String> timeList, int repeatList, List<String> notesList, int finishedTrips, int currentActiveTrip) {
        this.tripNameList = tripNameList;
        this.startPointList = startPointList;
        this.endPointList = endPointList;
        this.dateList = dateList;
        this.timeList = timeList;
        this.repeatList = repeatList;
        this.notesList = notesList;
        this.finishedTrips = finishedTrips;
        this.currentActiveTrip = currentActiveTrip;
    }



    @Override
    public List<String> getTripNameList() {
        return tripNameList;
    }

    @Override
    public void setTripNameList(List<String> tripNameList) {
        this.tripNameList = tripNameList;
    }

    @Override
    public List<String> getStartPointList() {
        return startPointList;
    }

    @Override
    public void setStartPointList(List<String> startPointList) {
        this.startPointList = startPointList;
    }

    @Override
    public List<String> getEndPointList() {
        return endPointList;
    }

    @Override
    public void setEndPointList(List<String> endPointList) {
        this.endPointList = endPointList;
    }

    @Override
    public List<String> getDateList() {
        return dateList;
    }

    @Override
    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    @Override
    public List<String> getTimeList() {
        return timeList;
    }

    @Override
    public void setTimeList(List<String> timeList) {
        this.timeList = timeList;
    }

    @Override
    public int getRepeatList() {
        return repeatList;
    }

    @Override
    public void setRepeatList(int repeatList) {
        this.repeatList = repeatList;
    }

    @Override
    public List<String> getNotesList() {
        return notesList;
    }

    @Override
    public void setNotesList(List<String> notesList) {
        this.notesList = notesList;
    }

    @Override
    public int getFinishedTrips() {
        return finishedTrips;
    }

    @Override
    public void setFinishedTrips(int finishedTrips) {
        this.finishedTrips = finishedTrips;
    }

    @Override
    public int getCurrentActiveTrip() {
        return currentActiveTrip;
    }

    @Override
    public void setCurrentActiveTrip(int currentActiveTrip) {
        this.currentActiveTrip = currentActiveTrip;
    }
}
