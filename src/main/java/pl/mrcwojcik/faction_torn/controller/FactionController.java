package pl.mrcwojcik.faction_torn.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mrcwojcik.faction_torn.models.entities.Faction;
import pl.mrcwojcik.faction_torn.service.FactionService;

@CrossOrigin
@RestController
public class FactionController {

    private final FactionService factionService;

    public FactionController(FactionService factionService) {
        this.factionService = factionService;
    }

    /** Basic endpoint to get information about faction like: name, days, gained respect
     * @return Faction */
    @GetMapping("/api/faction")
    public Faction getFactionInfo(){
        return factionService.getFactionInfo();
    }

}
