package com.dengo.web.api.controller;

import com.dengo.web.api.repository.EventRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * WEB API Controller
 *
 * Controller that handles all requests from client side
 * available request is /events that return JSON formatted String
 *
 * @author Sergey Petrovsky
 * @since 0.0.1
 */

@RestController
public class EventController {
    private static final Gson gson = new Gson();

    @Autowired
    private EventRepository eventRepository;

    @RequestMapping(value = "/events")
    public String getErrands() {
        return gson.toJson(eventRepository.getEvents());
    }
}
