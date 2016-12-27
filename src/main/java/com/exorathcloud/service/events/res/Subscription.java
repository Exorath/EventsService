package com.exorathcloud.service.events.res;

import io.reactivex.Observable;

/**
 * Created by toonsev on 12/27/2016.
 */
public interface Subscription {
    /**
     * Calling this will send the event to the subscribed service
     * @param event the event to transmit to the subscribed service
     */
    void onEvent(Event event);

    /**
     * Returns a stream of all subscribeRequests that are transmitted by the subbed service
     * @return a stream of subscribeRequests transmitted by subbed service
     */
    Observable<SubscribeRequest> getSubscribeRequestStream();
}
