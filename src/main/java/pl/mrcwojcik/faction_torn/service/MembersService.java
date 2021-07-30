package pl.mrcwojcik.faction_torn.service;

import pl.mrcwojcik.faction_torn.models.entities.Member;

import java.util.List;

public interface MembersService {

    List<Member> getMembersList();
    void updateMembersRespect();
    List<Member> sortMembersRespect();
    List<Member> getYeetList();
}
