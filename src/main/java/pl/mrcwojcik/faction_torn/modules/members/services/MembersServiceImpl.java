package pl.mrcwojcik.faction_torn.modules.members.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.mrcwojcik.faction_torn.modules.members.domain.Member;
import pl.mrcwojcik.faction_torn.modules.members.domain.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MembersServiceImpl implements MembersService {

    private final MemberRepository memberRepository;
    private final List<String> apiKeys = new ArrayList<>();
    private final MembersRequestsToApi membersRequestsToApi;

    public MembersServiceImpl(MemberRepository memberRepository, @Value("#{${torn.apis}}") List<String> keysFromSettings, MembersRequestsToApi membersRequestsToApi) {
        this.memberRepository = memberRepository;
        this.membersRequestsToApi = membersRequestsToApi;
        for (String key: keysFromSettings){
            apiKeys.add(key);
        }
    }

    @Override
    public List<Member> getMembersList() {
        return getFullMemberList();
    }

    @Override
    public List<Member> updateMembersRespect() {
        List<Member> memberList = getFullMemberList();
        for (Member m: memberList){
            getPersonalStats(m, apiKeys.get(1));
            setMemberLevelAndActivity(m, apiKeys.get(0));
            checkLastAction(m);
            memberRepository.save(m);
        }

        return memberList;
    }

    @Override
    public List<Member> updateMembers(String userKey){
        List<Member> memberList = getFullMemberList();
        apiKeys.add(userKey);
        for (int i = 0; i <= memberList.size()-1; i++){
            updateData(toggleKey(i, apiKeys), memberList.get(i));
            checkLastAction(memberList.get(i));
            memberRepository.save(memberList.get(i));
        }

        return memberList;
    }

    private void checkLastAction(Member member){
        if (member.getLastAction() >= 4){
            member.setYeet(true);
        } else {
            member.setYeet(false);
        }
    }

    private String toggleKey(int i, List<String> apiKeys){
        if (i <= 30){
            return apiKeys.get(0);
        } else if (i <= 60){
            return apiKeys.get(1);
        } else {
            return apiKeys.get(2);
        }
    }

    private void updateData(String key, Member member){
        getPersonalStats(member, key);
        setMemberLevelAndActivity(member, key);
    }


    private void setMemberLevelAndActivity(Member member, String key) {
        JSONObject json = membersRequestsToApi.makeRequestToApi("user/" + member.getTornId() + "?selections=profile&key=", key);
        member.setLevel(json.getInt("level"));
        JSONObject lastAction = json.getJSONObject("last_action");
        member.setLastAction(lastAction.getInt("timestamp"));
    }

    private void getPersonalStats(Member member, String key) {
        JSONObject json = membersRequestsToApi.makeRequestToApi("user/" + member.getTornId() + "?selections=personalstats&key=", key);
        member.setRpForFaction(json.getJSONObject("personalstats").getInt("respectforfaction"));
    }

    public List<Member> getFullMemberList(){
        return memberRepository.findByOrderByLevelDesc();
    }

    @Override
    public List<Member> getYeetList() {
        return memberRepository.findAllByYeetIsTrue();
    }
}
