package pl.mrcwojcik.faction_torn.modules.events.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Event findFirstByOrderByEventDateAsc();
}
