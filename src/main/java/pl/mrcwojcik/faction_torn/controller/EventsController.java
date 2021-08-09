package pl.mrcwojcik.faction_torn.controller;

import org.springframework.web.bind.annotation.*;
import pl.mrcwojcik.faction_torn.models.entities.Event;
import pl.mrcwojcik.faction_torn.service.EventService;

import java.time.Instant;
import java.util.List;

@CrossOrigin
@RestController
public class EventsController {

    private final EventService eventService;

    public EventsController(EventService eventService) {
        this.eventService = eventService;
    }

    /** This endpoint gives list of all events added by council or mentors of faction
     * @return List<Event> */
    @GetMapping("/api/events")
    public List<Event> getEventsList(){
        return eventService.getAllEvents();
    }

    /** Endpoint that clears events, that are older than date when endpoint is used
     * @return updated List<Event> */
    @GetMapping("/api/events/clear")
    public List<Event> deleteOldEvents(){
        eventService.clearOldEvents(Instant.now());
        return eventService.getAllEvents();
    }

    /** Return event closest to current time */
    @GetMapping("/api/event/closest")
    public Event getClosestEvent(){
        eventService.clearOldEvents(Instant.now());
        return eventService.getClosestEvent();
    }

    /** Endpoint with help to add new event to database
     * @param event
     * @return added Event */
    @PostMapping("/api/event/add")
    public Event addEvent(@RequestBody Event event){
        return eventService.addEvent(event);
    }

}
