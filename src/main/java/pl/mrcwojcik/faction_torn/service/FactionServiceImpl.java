package pl.mrcwojcik.faction_torn.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.mrcwojcik.faction_torn.models.entities.Faction;
import pl.mrcwojcik.faction_torn.models.entities.Member;
import pl.mrcwojcik.faction_torn.repositories.FactionRepository;
import pl.mrcwojcik.faction_torn.repositories.MemberRepository;
import pl.mrcwojcik.faction_torn.repositories.ReportRepository;
import pl.mrcwojcik.faction_torn.utils.ReadJson;

import java.util.ArrayList;
import java.util.List;


@Service
public class FactionServiceImpl implements FactionService {

    private final FactionRepository factionRepository;
    private final MemberRepository memberRepository;
    private final ReadJson readJson;
    private final ReportRepository reportRepository;
    private final String myApiKey;

    public FactionServiceImpl(FactionRepository factionRepository, MemberRepository memberRepository, ReadJson readJson, ReportRepository reportRepository, @Value("${torn.api.koziolkuj}") String myApiKey) {
        this.factionRepository = factionRepository;
        this.memberRepository = memberRepository;
        this.readJson = readJson;
        this.reportRepository = reportRepository;
        this.myApiKey = myApiKey;
    }

    //    TODO: exclude API request to RequestToApi.class
    @Override
    public Faction updateFaction() {
        try {
            JSONObject json = readJson.readJsonFromUrl("https://api.torn.com/faction/?selections=&key=" + myApiKey);
            return saveFactionData(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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
                member.setYellowFlag(0);
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
        }
    }

    private void updateMemberData(Object keyValue, Member member){
        if (keyValue instanceof JSONObject){
            member.setDaysInFaction(((JSONObject) keyValue).getInt("days_in_faction"));
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
                if (reportRepository.findByMember_TornId(m.getTornId()) != null){
                    reportRepository.delete(reportRepository.findByMember_TornId(m.getTornId()));
                }
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
