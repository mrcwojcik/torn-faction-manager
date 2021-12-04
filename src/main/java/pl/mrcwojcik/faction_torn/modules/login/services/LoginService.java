package pl.mrcwojcik.faction_torn.modules.login.services;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import pl.mrcwojcik.faction_torn.modules.members.domain.Member;
import pl.mrcwojcik.faction_torn.modules.faction.domain.FactionRepository;
import pl.mrcwojcik.faction_torn.modules.members.domain.MemberRepository;

import java.util.List;
import java.util.Objects;

@Service
public class LoginService {

    private final MemberRepository memberRepository;
    private final LoginApiRequest loginApiRequest;
    private final FactionRepository factionRepository;

    public LoginService(MemberRepository memberRepository, LoginApiRequest loginApiRequest, FactionRepository factionRepository) {
        this.memberRepository = memberRepository;
        this.loginApiRequest = loginApiRequest;
        this.factionRepository = factionRepository;
    }

    public Member login(String apiKey){
        return getUser(apiKey);
    }

    private Member getUser(String apiKey){
        JSONObject jsonObject = loginApiRequest.makeRequestToApi("user/?selections=&key=", apiKey);
        Integer playerId = jsonObject.getInt("player_id");
        Member member = userIsInFaction(playerId);
        if (member == null){
            return null;
        }

        if (checkPosition(member)){
            return member;
        }

        return null;
    }

    private Member userIsInFaction(Integer playerId){
        List<Member> memberList = memberRepository.findAll();
        for (Member member : memberList){
            if (Objects.equals(member.getTornId(), playerId)){
                return member;
            }
        }

        return null;
    }

    private boolean checkPosition(Member member) {
        String memberRole = member.getRoles();
        return memberRole.equals("Board") || memberRole.equals("Council") || memberRole.equals("Mentor") || memberRole.equals("Leader") || memberRole.equals("Co-leader");
    }

}
