package com.mma.calendar.model;

/**
 * Created by Toni on 04-Feb-15.
 */
public class UserEvent {
    private long userEventID;
    private long fkUserID;
    private long fkEventID;

    public UserEvent() {
        this(0, 0, 0);
    }

    public UserEvent(long newUserEventID, long newFkUserID, long newFkEventID) {
        setUserEventID(newUserEventID);
        setFkUserID(newFkUserID);
        setFkEventID(newFkEventID);
    }

    public void setUserEventID(long userEventID) {
        this.userEventID = userEventID;
    }

    public void setFkUserID(long fkUserID) {
        this.fkUserID = fkUserID;
    }

    public void setFkEventID(long fkEventID) {
        this.fkEventID = fkEventID;
    }

    public long getUserEventID() {
        return userEventID;
    }

    public long getFkUSerID() {
        return fkUserID;
    }

    public long getFkEventID() {
        return fkEventID;
    }
}
