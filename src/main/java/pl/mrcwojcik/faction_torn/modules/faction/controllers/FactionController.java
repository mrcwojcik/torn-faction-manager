package pl.mrcwojcik.faction_torn.modules.faction.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.mrcwojcik.faction_torn.modules.faction.domain.Faction;
import pl.mrcwojcik.faction_torn.modules.faction.services.FactionService;
import pl.mrcwojcik.faction_torn.modules.faction.services.UpdateService;
import pl.mrcwojcik.faction_torn.modules.members.domain.Member;
import pl.mrcwojcik.faction_torn.modules.members.services.MembersService;

import java.util.List;

@CrossOrigin
@RestController
public class FactionController {

    private final FactionService factionService;
    private final MembersService membersService;
    private final UpdateService updateService;

    public FactionController(FactionService factionService, MembersService membersService, UpdateService updateService) {
        this.factionService = factionService;
        this.membersService = membersService;
        this.updateService = updateService;
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

    @GetMapping("/api/update")
    public List<Member> updateData(@RequestHeader("apiKey") String userKey){
        String key = userKey.replaceAll("\"", "").replaceAll("\"","");
        return updateService.updateData(key);
    }

}
