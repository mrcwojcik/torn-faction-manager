package pl.mrcwojcik.faction_torn.cron;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.mrcwojcik.faction_torn.service.EventService;
import pl.mrcwojcik.faction_torn.service.FactionService;
import pl.mrcwojcik.faction_torn.service.MembersService;

import java.time.Instant;

@Component
public class CronTasks {

    private final MembersService membersService;
    private final FactionService factionService;
    private final EventService eventService;

    public CronTasks(MembersService membersService, FactionService factionService, EventService eventService) {
        this.membersService = membersService;
        this.factionService = factionService;
        this.eventService = eventService;
    }

    /** Cron task which do three things:
     * 1. Update basic faction information: days, respect
     * 2. Update members in faction - they basic stats
     * 3. Clear older events
     * Cron is run every day at 5am. */
    @Scheduled(cron = "0 * 5 * * *")
    public void updateUsers(){
        factionService.updateFaction();
        membersService.updateMembersRespect();
        eventService.clearOldEvents(Instant.now());
    }
}
