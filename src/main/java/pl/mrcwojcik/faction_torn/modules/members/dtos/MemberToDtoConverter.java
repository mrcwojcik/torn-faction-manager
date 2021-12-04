package pl.mrcwojcik.faction_torn.modules.members.dtos;

import org.springframework.stereotype.Component;
import pl.mrcwojcik.faction_torn.modules.members.domain.Member;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemberToDtoConverter {

    public List<MemberDto> setMemberDto(List<Member> members){
        List<MemberDto> memberDtos = new ArrayList<>();
        for (Member m: members){
            MemberDto memberDto = createMemberDto(m);
            memberDtos.add(memberDto);
        }

        return memberDtos;
    }

    private MemberDto createMemberDto(Member m) {
        MemberDto memberDto = new MemberDto();
        memberDto.setDaysInFaction(m.getDaysInFaction());
        memberDto.setRoles(m.getRoles());
        memberDto.setUsername(m.getUsername());
        memberDto.setTornId(m.getTornId());
        memberDto.setYeet(m.isYeet());
        memberDto.setLevel(m.getLevel());
        memberDto.setRpForFaction(m.getRpForFaction());
        memberDto.setLastAction(m.getLastAction());
        memberDto.setClosestMerit();
        memberDto.setDistanceToMerit();
        return memberDto;
    }

}
