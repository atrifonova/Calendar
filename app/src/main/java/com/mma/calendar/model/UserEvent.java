package com.mma.calendar.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Toni on 04-Feb-15.
 */

@ParseClassName("UserEvent")

public class UserEvent extends ParseObject {

    private long userEventID;
    private long fkUserID;
    private long fkEventID;

}
