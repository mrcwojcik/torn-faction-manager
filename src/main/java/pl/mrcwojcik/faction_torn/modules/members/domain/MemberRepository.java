package pl.mrcwojcik.faction_torn.modules.members.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByOrderByLevelDesc();
    Member findByTornId(Integer tornId);
    List<Member> findAllByYeetIsTrue();
}
