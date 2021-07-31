package pl.mrcwojcik.faction_torn.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mrcwojcik.faction_torn.models.entities.Member;
import pl.mrcwojcik.faction_torn.service.MembersService;

import java.util.List;

@CrossOrigin
@RestController
public class MembersApiController {

    private final MembersService membersService;

    public MembersApiController(MembersService membersService) {
        this.membersService = membersService;
    }

    /** Endpoint with return list of all members in faction
     * @return List<Member>*/
    @GetMapping("/api/members")
    public List<Member> getMembersList(){
        return membersService.getMembersList();
    }

}
