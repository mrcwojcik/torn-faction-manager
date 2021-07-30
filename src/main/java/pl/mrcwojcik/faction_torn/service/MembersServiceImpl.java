package pl.mrcwojcik.faction_torn.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.mrcwojcik.faction_torn.models.entities.Member;
import pl.mrcwojcik.faction_torn.repositories.MemberRepository;
import pl.mrcwojcik.faction_torn.utils.ReadJson;

import java.util.List;

@Service
public class MembersServiceImpl implements MembersService {

    private final MemberRepository memberRepository;
    private final ReadJson readJson;
    private final List<String> apiKeys;

    public MembersServiceImpl(MemberRepository memberRepository, ReadJson readJson, @Value("#{${torn.apis}}") List<String> apiKeys) {
        this.memberRepository = memberRepository;
        this.readJson = readJson;
        this.apiKeys = apiKeys;
    }

    @Override
    public List<Member> getMembersList() {
        return getFullMemberList();
    }

    @Override
    public void updateMembersRespect() {
        List<Member> memberList = getFullMemberList();
        for (Member m: memberList){
            m.setRpForFaction(getPersonalStats(m.getTornId()));
            setMemberLevelAndActivity(m);
            if (m.getLastAction() >= 4){
                m.setYeet(true);
            } else {
                m.setYeet(false);
            }
            memberRepository.save(m);
        }
    }

    @Override
    public List<Member> sortMembersRespect() {
        return memberRepository.findByOrderByRpForFactionDesc();
    }

    // TODO: exclude api request to RequestToApi.class
    private void setMemberLevelAndActivity(Member member) {
        try{
            JSONObject json = readJson.readJsonFromUrl("https://api.torn.com/user/" + member.getTornId() + "?selections=profile&key=" + apiKeys.get(0));
            member.setLevel(json.getInt("level"));
            JSONObject lastAction = json.getJSONObject("last_action");
            member.setLastAction(lastAction.getInt("timestamp"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // TODO: exclude api request to RequestToApi.class
    private Integer getPersonalStats(Integer tornId) {
        try{
            JSONObject json = readJson.readJsonFromUrl("https://api.torn.com/user/" + tornId + "?selections=personalstats&key=" + apiKeys.get(1));
            return readRespectFromApi(json);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private Integer readRespectFromApi(JSONObject json) {
        JSONObject personalStats = json.getJSONObject("personalstats");
        return personalStats.getInt("respectforfaction");
    }


    public List<Member> getFullMemberList(){
        return memberRepository.findByOrderByLevelDesc();
    }

    @Override
    public List<Member> getYeetList() {
        return memberRepository.findAllByYeetIsTrue();
    }
}
