package pl.mrcwojcik.faction_torn.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mrcwojcik.faction_torn.models.entities.Faction;
import pl.mrcwojcik.faction_torn.models.entities.Member;
import pl.mrcwojcik.faction_torn.service.FactionService;
import pl.mrcwojcik.faction_torn.service.MembersService;

import java.util.List;

@CrossOrigin
@RestController
public class FactionController {

    private final FactionService factionService;
    private final MembersService membersService;

    public FactionController(FactionService factionService, MembersService membersService) {
        this.factionService = factionService;
        this.membersService = membersService;
    }

    /** Basic endpoint to get information about faction like: name, days, gained respect
     * @return Faction */
    @GetMapping("/api/faction")
    public Faction getFactionInfo(){
        return factionService.getFactionInfo();
    }
    /** Endpoint to update basic faction information
     * @return Faction with updated data */
    @GetMapping("/api/faction/update")
    public Faction updateFaction(){
        return factionService.updateFaction();
    }

    /** Updating information about members: respect gained, days in faction, level. It's connected with members, but it's more like administration thing to do.
     * @return List<Member>*/
    @GetMapping("/api/members/update")
    public List<Member> updateFactionMembers(){
        return membersService.updateMembersRespect();
    }

}
