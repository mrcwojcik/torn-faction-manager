package pl.mrcwojcik.faction_torn.modules.members.services;

import pl.mrcwojcik.faction_torn.modules.members.domain.Member;

import java.util.List;

public interface MembersService {

    List<Member> getMembersList();
    List<Member> updateMembersRespect();
    List<Member> getYeetList();
    List<Member> updateMembers(String userKey);
}
