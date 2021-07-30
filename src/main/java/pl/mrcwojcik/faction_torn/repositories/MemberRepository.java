package pl.mrcwojcik.faction_torn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mrcwojcik.faction_torn.models.entities.Member;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByOrderByLevelDesc();
    Member findByTornId(Integer tornId);
    List<Member> findByOrderByRpForFactionDesc();
    List<Member> findAllByYeetIsTrue();
}
