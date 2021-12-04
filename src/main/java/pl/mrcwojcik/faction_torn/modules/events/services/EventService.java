package pl.mrcwojcik.faction_torn.modules.events.services;

import org.springframework.stereotype.Service;
import pl.mrcwojcik.faction_torn.modules.events.domain.Event;
import pl.mrcwojcik.faction_torn.modules.events.domain.EventRepository;

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
        clearOldEvents(Instant.now());
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

    public void deleteEvent(long id) {
        eventRepository.delete(eventRepository.findById(id).get());
    }
}
