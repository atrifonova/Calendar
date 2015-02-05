package com.mma.calendar.model;

/**
 * Created by Toni on 04-Feb-15.
 */
public class Event {
    private long eventID;
    private String eventTitle;
    private String eventDescription;
    private String eventLocation;
    private String eventPhoto;
    private String eventColor;
    private String eventStartDate;
    private String eventEndDate;
    private String eventStartTime;
    private String eventEndTime;

    public Event() {
        this(0, "", "", "", "", "", "", "", "", "");
    }

    public Event(long newEventID, String newEventTitle, String newEventDescription, String newEventLocation, String newEventPhoto, String newEventColor, String newEventStartDate, String newEventEndDate, String newEventStartTime, String newEventEndTime) {
        setEventID(newEventID);
        setEventTitle(newEventTitle);
        setEventDescription(newEventDescription);
        setEventLocation(newEventLocation);
        setEventPhoto(newEventPhoto);
        setEventColor(newEventColor);
        setEventStartDate(newEventStartDate);
        setEventEndDate(newEventEndDate);
        setEventStartTime(newEventStartTime);
        setEventEndTime(newEventEndTime);
    }

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String newEventTitle) {
        if (newEventTitle != null) {
            eventTitle = newEventTitle;
        } else {
            eventTitle = "";
        }
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String newEventDescription) {
        if (newEventDescription != null) {
            eventDescription = newEventDescription;
        } else {
            eventDescription = "";
        }
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String newEventLocation) {
        if (newEventLocation != null) {
            eventLocation = newEventLocation;
        } else {
            eventLocation = "";
        }
    }

    public String getEventPhoto() {
        return eventPhoto;
    }

    public void setEventPhoto(String newEventPhoto) {
        if (newEventPhoto != null) {
            eventPhoto = newEventPhoto;
        } else {
            eventPhoto = "";
        }
    }

    public String getEventColor() {
        return eventColor;
    }

    public void setEventColor(String newEventColor) {
        if (newEventColor != null) {
            eventColor = newEventColor;
        } else {
            eventColor = "";
        }
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String newEventStartDate) {
        if (newEventStartDate != null) {
            eventStartDate = newEventStartDate;
        } else {
            eventStartDate = "";
        }
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String newEventEndDate) {
        if (newEventEndDate != null) {
            eventEndDate = newEventEndDate;
        } else {
            eventEndDate = "";
        }
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String newEventStartTime) {
        if (newEventStartTime != null) {
            eventStartTime = newEventStartTime;
        } else {
            eventStartTime = "";
        }
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String newEventEndTime) {
        if (newEventEndTime != null) {
            eventEndTime = newEventEndTime;
        } else {
            eventEndTime = "";
        }
    }
}
