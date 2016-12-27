/*
 * Copyright 2016 Exorath
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.exorathcloud.service.events;

import com.exorath.service.commons.portProvider.PortProvider;
import com.exorathcloud.service.events.res.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import spark.Route;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static spark.Spark.*;

/**
 * Created by Shortninja66 on 12/26/2016.
 */
public class Transport {
    private static final Gson GSON = new Gson();

    private static Service service;

    public static void setup(Service service, PortProvider portProvider) {
        Transport.service = service;

        port(portProvider.getPort());

        post("/event/:eventId", getPostEventRoute(service), GSON::toJson);
        webSocket("/subscribe", EventSocket.class);
        init();
    }

    public static Route getPostEventRoute(Service service) {
        return (req, res) -> {
            try {
                Event event = GSON.fromJson(req.body(), Event.class);
                event.setEventId(req.params("eventId"));
                return service.publishEvent(event);
            }catch (Exception e) {
                e.printStackTrace();
                return new Success(false, e.getMessage());
            }
        };
    }

    public static class SubscribeWebSocket extends WebSocketAdapter implements Subscription {
        private Long lastTimeout;
        private PublishSubject<SubscribeRequest> requestPublishSubject = PublishSubject.create();

        @Override
        public boolean onEvent(Event event) {
            try {
                getRemote().sendString(GSON.toJson(event));
            }catch (IOException e){
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        public Observable<SubscribeRequest> getSubscribeRequestStream() {
            return requestPublishSubject;
        }

        @Override
        public Completable getCompletable() {
            return Completable.fromObservable(requestPublishSubject);
        }

        @Override
        public void onWebSocketConnect(Session sess) {
            super.onWebSocketConnect(sess);
            //whenever an action is received, propagate it to the websocket
            service.subscribe(this);
        }

        @Override
        public void onWebSocketText(String message) {
            super.onWebSocketText(message);
            //Try and parse the websocket
            if (handlePing(message))//if this is a ping message, handle it and return;
                return;
            //if it's not a ping, check if it's a subscribe request and emit it
            try {
                JsonObject messageObj = GSON.fromJson(message, JsonObject.class);
                if (messageObj.has("subscribe"))
                    requestPublishSubject.onNext(GSON.fromJson(messageObj.get("subscribe"), SubscribeRequest.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Handles the ping to keep the connection open (should be pinged within 300 seconds)
        private boolean handlePing(String message) {
            try {
                setLastTimeout(Long.valueOf(message));
                if(!closeIfTimeout()) {
                    getRemote().sendString("{}");//send pong
                    Observable.create(sub -> {
                        sub.onNext(new Object());
                        sub.onComplete();
                    }).delay(getLastTimeout() - System.currentTimeMillis(), TimeUnit.MILLISECONDS).doOnNext(o ->
                            closeIfTimeout()).subscribe();
                }
                return true;
            } catch (NumberFormatException e) {
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }
        }

        private synchronized void setLastTimeout(long timeout) {
            this.lastTimeout = timeout;
        }

        private synchronized long getLastTimeout() {
            return lastTimeout;
        }

        private synchronized boolean closeIfTimeout() {
            long timeout = getLastTimeout() - System.currentTimeMillis();
            System.out.println(timeout);
            System.out.println("closed?");
            if (timeout < 0) {
                System.out.println("closed");
                onWebSocketClose(408, "Connection timed out");
                return true;
            }
            return false;
        }


        @Override
        public void onWebSocketClose(int statusCode, String reason) {
            requestPublishSubject.onComplete();//close all subscriptions
            if (getSession() != null && getSession().isOpen())
                getSession().close();
            super.onWebSocketClose(statusCode, reason);
        }

        @Override
        public void onWebSocketError(Throwable cause) {
            //print a websocket error
            super.onWebSocketError(cause);
            cause.printStackTrace(System.err);
        }
    }
}