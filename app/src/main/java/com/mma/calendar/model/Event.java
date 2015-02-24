package com.mma.calendar.model;

import com.mma.calendar.constants.Constants;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Toni on 04-Feb-15.
 */
@ParseClassName("Event")

public class Event extends ParseObject {
/*
    private String eventLocation;
*/

    public Event () {

    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle (String title) {
        put("title", title);
    }

    public String getDescription(){
        return getString("description");
    }

    public void setDescription(String description){
        put("description", description);
    }
/*
    public void setLocation (ParseGeoPoint location) {
        put("location", location);
    }

    public ParseGeoPoint getLocation () {
        return getParseGeoPoint("location");
    }*/

    public String getStartDate() {
        return getString("startDate");
    }

    public void setStartDate(String startDate) {
        put("startDate", startDate);
    }

    public String getEndDate() {
        return getString("endDate");
    }

    public void setEndDate(String endDate) {
        put("endDate", endDate);
    }

    public String getStartTime() {
        return getString("startTime");
    }

    public void setStartTime(String startTime) {
        put("startTime", startTime);
    }

    public String getEndTime() {
        return getString("endTime");
    }

    public void setEndTime(String endTime) {
        put("endTime", endTime);
    }

    public void setUser(ParseUser currentUser) {
        put("user", currentUser);
    }


    public String getAddress() {
        return getString(Constants.ADDRESS);
    }

    public void setAddress(String address) {
        put(Constants.ADDRESS, address);
    }


    public void setLat(double lat) {
        put(Constants.LATITUDE, lat);
    }


    public void setLon(double lot) {
        put(Constants.LONGITUDE, lot);
    }


    public double getLot() {
        return getDouble(Constants.LONGITUDE);
    }

    public double getLat() {
        return getDouble(Constants.LATITUDE);
    }

    public void setInviteUsers(String inviteUsers){
        put("inviteUsers", inviteUsers);
    }

    public String getInviteUser(){
        return getString("inviteUsers");
    }

}
