package pl.mrcwojcik.faction_torn.modules.faction.services;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import pl.mrcwojcik.faction_torn.modules.members.domain.Member;
import pl.mrcwojcik.faction_torn.modules.members.services.MembersService;

import java.util.List;

@Service
public class UpdateService {

    private final UpdateRequestsToApi updateRequestsToApi;
    private final FactionService factionService;
    private final MembersService membersService;

    public UpdateService(UpdateRequestsToApi updateRequestsToApi, FactionService factionService, MembersService membersService) {
        this.updateRequestsToApi = updateRequestsToApi;
        this.factionService = factionService;
        this.membersService = membersService;
    }

    public List<Member> updateData(String userKey){
        updateFaction(userKey);
        return updateMembers(userKey);
    }

    private void updateFaction(String userKey){
        JSONObject jsonFaction = updateRequestsToApi.makeRequestToApi("faction/?selections=&key=", userKey);
        factionService.saveFactionData(jsonFaction);
    }

    private List<Member> updateMembers(String userKey){
        return membersService.updateMembers(userKey);
    }

}
