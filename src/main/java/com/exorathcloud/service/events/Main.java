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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Shortninja66 on 12/26/2016.
 */
public class Main {
    private static Service service;
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Main.service = null;//Todo assign service implementation here
        LOG.info("Service " + Main.service.getClass() + " instantiated");
        Transport.setup(Main.service, PortProvider.getEnvironmentPortProvider());
        LOG.info("HTTP transport setup");
    }
}
