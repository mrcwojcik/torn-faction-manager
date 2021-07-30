package pl.mrcwojcik.faction_torn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mrcwojcik.faction_torn.models.entities.Report;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByOrderByHitsDesc();
    Report findByMember_TornId(Integer memberTornId);

}
