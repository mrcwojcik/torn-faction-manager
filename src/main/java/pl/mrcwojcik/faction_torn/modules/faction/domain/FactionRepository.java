package pl.mrcwojcik.faction_torn.modules.faction.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactionRepository extends JpaRepository<Faction, Long> {

    Faction findFirstByOrderByAge();

}
