package pl.mrcwojcik.faction_torn.modules.members.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mrcwojcik.faction_torn.modules.members.dtos.MemberDto;
import pl.mrcwojcik.faction_torn.modules.members.services.MeritService;

import java.util.List;

@CrossOrigin
@RestController
public class MeritController {

    private final MeritService meritService;

    public MeritController(MeritService meritService) {
        this.meritService = meritService;
    }

    /** Endpoint with return list of members with closestMerits and distance to that merit.
     *
     * @return List<MemberDto>
     */
    @GetMapping("/api/merit")
    public List<MemberDto> getMeritList(){
        return meritService.getMeritList();
    }

}
