package com.exorathcloud.service.events.res;


/**
 * Represents an event Instance.
 * Created by toonsev on 12/27/2016.
 */
public class Event {
    private String eventId;
    private Object meta;

    /**
     * Gets the type id of this event (fe. 'join')
     * @return the type id of this event
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * The meta represents event specific meta, this should be serializable with {@link com.google.gson.Gson}
     * @return the event specific meta
     */
    public Object getMeta() {
        return meta;
    }
}
