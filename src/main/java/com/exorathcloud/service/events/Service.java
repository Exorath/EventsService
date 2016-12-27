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

import com.exorathcloud.service.events.res.Event;
import com.exorathcloud.service.events.res.Subscription;
import com.exorathcloud.service.events.res.Success;

/**
 * Created by Shortninja66 on 12/26/2016.
 */
public interface Service {

   //Success publishEvent(String eventId, EventMeta meta);
    Success publishEvent(Event event);
    /**
     * @return Whether or not the subscription was successful.
     */
    // boolean subscribe(String[] events, String serviceId, String instanceId);
    boolean subscribe(Subscription subscription);
}