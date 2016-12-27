package com.exorathcloud.service.events.res;


/**
 * Created by toonsev on 12/27/2016.
 */
public class SubscribeRequest {
    private String[] events;
    private String serviceId;
    private String instanceId;


    /**
     * Gets a list of events this request likes to subscribe to
     * @return a list of events to subscribe to
     */
    public String[] getEvents() {
        return events;
    }

    /**
     * Gets the serviceId of the requesting service (this is an id that is unique per service, not per service instance!)
     * @return the serviceId of the requester
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Gets an instanceId of the requesting service, this is unique per service instance.
     * @return the instanceId of the requester
     */
    public String getInstanceId() {
        return instanceId;
    }
}
