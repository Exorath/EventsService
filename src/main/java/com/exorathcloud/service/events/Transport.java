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
import com.exorathcloud.service.events.res.EventMeta;
import com.exorathcloud.service.events.res.EventSocket;
import com.exorathcloud.service.events.res.Success;
import com.google.gson.Gson;
import spark.Route;

import static spark.Spark.*;

/**
 * Created by Shortninja66 on 12/26/2016.
 */
public class Transport {

    private static final Gson GSON = new Gson();

    public static void setup(Service service, PortProvider portProvider) {
        port(portProvider.getPort());

        post("/event/:eventId", getPostEventRoute(service), GSON::toJson);
        webSocket("/subscribe", EventSocket.class);
        init();
    }

    public static Route getPostEventRoute(Service service) {
        return (req, res) -> {
            EventMeta meta = null;

            try {
                meta = GSON.fromJson(req.body(), EventMeta.class);
            }catch (Exception e) {
                e.printStackTrace();
                return new Success(false, e.getMessage());
            }

            return service.publishEvent(req.params("eventId"), meta);
        };
    }

}