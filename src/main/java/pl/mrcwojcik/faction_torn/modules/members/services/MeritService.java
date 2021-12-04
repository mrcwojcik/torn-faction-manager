package pl.mrcwojcik.faction_torn.modules.members.services;

import org.springframework.stereotype.Service;
import pl.mrcwojcik.faction_torn.modules.members.dtos.MemberDto;
import pl.mrcwojcik.faction_torn.modules.members.dtos.MemberToDtoConverter;

import java.util.List;

@Service
public class MeritService {

    private final MembersService membersService;
    private final MemberToDtoConverter memberToDtoConverter;

    public MeritService(MembersService membersService, MemberToDtoConverter memberToDtoConverter) {
        this.membersService = membersService;
        this.memberToDtoConverter = memberToDtoConverter;
    }

    public List<MemberDto> getMeritList(){
        return memberToDtoConverter.setMemberDto(membersService.getMembersList());
    }

}
