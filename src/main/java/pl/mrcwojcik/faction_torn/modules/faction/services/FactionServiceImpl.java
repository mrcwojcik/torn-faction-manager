package pl.mrcwojcik.faction_torn.modules.faction.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.mrcwojcik.faction_torn.modules.faction.domain.Faction;
import pl.mrcwojcik.faction_torn.modules.faction.domain.FactionRepository;
import pl.mrcwojcik.faction_torn.modules.members.domain.Member;
import pl.mrcwojcik.faction_torn.modules.members.domain.MemberRepository;

import java.util.ArrayList;
import java.util.List;


@Service
public class FactionServiceImpl implements FactionService {

    private final FactionRepository factionRepository;
    private final MemberRepository memberRepository;
    private final String myApiKey;
    private final UpdateRequestsToApi updateRequestsToApi;


    public FactionServiceImpl(FactionRepository factionRepository, MemberRepository memberRepository, @Value("${torn.api.koziolkuj}") String myApiKey, UpdateRequestsToApi updateRequestsToApi) {
        this.factionRepository = factionRepository;
        this.memberRepository = memberRepository;
        this.myApiKey = myApiKey;
        this.updateRequestsToApi = updateRequestsToApi;
    }

    @Override
    public Faction updateFaction() {
        JSONObject json = updateRequestsToApi.makeRequestToApi("faction/?selections=&key=", myApiKey);
        return saveFactionData(json);
    }

    @Override
    public Faction saveFactionData(JSONObject json){
        Faction faction = new Faction();
        faction.setTorn_id(json.get("ID").toString());
        faction.setAge(json.get("age").toString());
        faction.setBestchain(json.get("best_chain").toString());
        faction.setName(json.get("name").toString());
        faction.setRespect(json.get("respect").toString());
        factionRepository.save(faction);
        saveFactionMembers(json.getJSONObject("members"));
        clearMemberOutsideFaction(json.getJSONObject("members"));
        return faction;
    }

    private void saveFactionMembers(JSONObject json) {
        json.keySet().forEach(keyStr -> {
            Object keyValue = json.get(keyStr);
            Member memberExist = getMemberById(Integer.valueOf(keyStr));
            if (memberExist == null){
                Member member = new Member();
                member.setTornId(Integer.valueOf(keyStr));
                member.setYeet(false);
                saveDataToMember(keyValue, member);
                memberRepository.save(member);
            } else {
                updateMemberData(keyValue, memberExist);
                memberRepository.save(memberExist);
            }
        });
    }

    private void saveDataToMember(Object keyValue, Member member) {
        if (keyValue instanceof JSONObject){
            member.setUsername(((JSONObject) keyValue).get("name").toString());
            member.setDaysInFaction(((JSONObject) keyValue).getInt("days_in_faction"));
            member.setRoles(((JSONObject) keyValue).getString("position"));
        }
    }

    private void updateMemberData(Object keyValue, Member member){
        if (keyValue instanceof JSONObject){
            member.setDaysInFaction(((JSONObject) keyValue).getInt("days_in_faction"));
            member.setRoles(((JSONObject) keyValue).getString("position"));
        }
    }

    private Member getMemberById(Integer tornId){
        return memberRepository.findByTornId(tornId);
    }

    private void clearMemberOutsideFaction(JSONObject jsonObject) {
        List<Member> membersFromDb = memberRepository.findByOrderByLevelDesc();
        List<Integer> keysFromApi = new ArrayList<>();
        jsonObject.keySet().forEach(keyStr -> {
            keysFromApi.add(Integer.valueOf(keyStr));
        });

        for (Member m : membersFromDb){
            if (!keysFromApi.contains(m.getTornId())){
                memberRepository.delete(m);
            }
        }
    }

    @Override
    public Faction getFactionInfo() {
        Faction faction = factionRepository.findFirstByOrderByAge();
        if (faction == null){
            updateFaction();
        }
        return factionRepository.findFirstByOrderByAge();
    }
}
