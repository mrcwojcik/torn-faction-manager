package pl.mrcwojcik.faction_torn.controller;

import org.springframework.web.bind.annotation.*;
import pl.mrcwojcik.faction_torn.models.entities.Event;
import pl.mrcwojcik.faction_torn.models.entities.Faction;
import pl.mrcwojcik.faction_torn.models.entities.Member;
import pl.mrcwojcik.faction_torn.service.EventService;
import pl.mrcwojcik.faction_torn.service.FactionService;
import pl.mrcwojcik.faction_torn.service.MembersService;

import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
public class ApiController {

    private final FactionService factionService;
    private final MembersService membersService;
    private final EventService eventService;

    public ApiController(FactionService factionService, MembersService membersService, EventService eventService) {
        this.factionService = factionService;
        this.membersService = membersService;
        this.eventService = eventService;
    }

//    TODO: Exclude endpoints to separate controllers: Members, Events

    /** Basic endpoint to get information about faction like: name, days, gained respect
     * @return Faction */
    @GetMapping("/api/faction")
    public Faction getFactionInfo(){
        return factionService.getFactionInfo();
    }

    /** Endpoint with return list of all members in faction
     * @return List<Member>*/
    @GetMapping("/api/members")
    public List<Member> getMembersList(){
        return membersService.getMembersList();
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
        eventService.clearOldEvents(new Date());
        return eventService.getAllEvents();
    }

    /** Endpoint with help to add new event to database
     * @param event
     * @return added Event */
    @PostMapping("/api/event/add")
    public Event addEvent(@RequestBody Event event){
        return eventService.addEvent(event);
    }
}
