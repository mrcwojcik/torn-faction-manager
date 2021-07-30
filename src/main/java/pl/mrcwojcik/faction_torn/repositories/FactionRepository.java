package pl.mrcwojcik.faction_torn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mrcwojcik.faction_torn.models.entities.Faction;

@Repository
public interface FactionRepository extends JpaRepository<Faction, Long> {

    Faction findFirstByOrderByAge();

}
