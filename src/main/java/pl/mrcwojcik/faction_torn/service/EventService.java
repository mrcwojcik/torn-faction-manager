package pl.mrcwojcik.faction_torn.service;

import org.springframework.stereotype.Service;
import pl.mrcwojcik.faction_torn.models.entities.Event;
import pl.mrcwojcik.faction_torn.repositories.EventRepository;

import java.time.Instant;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event addEvent(Event event){
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents(){
        return eventRepository.findAll();
    }

    public void clearOldEvents(Instant now){
        List<Event> allEvents = eventRepository.findAll();
        for (Event ev : allEvents){
            if (ev.getEventDate().isBefore(now)){
                eventRepository.delete(ev);
            }
        }
    }

    public Event getClosestEvent(){
        return eventRepository.findFirstByOrderByEventDateAsc();
    }


}
