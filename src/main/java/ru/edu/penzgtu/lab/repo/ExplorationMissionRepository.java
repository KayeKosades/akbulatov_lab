package ru.edu.penzgtu.lab.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edu.penzgtu.lab.entity.ExplorationMission;
import ru.edu.penzgtu.lab.entity.enums.MissionStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExplorationMissionRepository extends JpaRepository<ExplorationMission, Long> {

    Optional<ExplorationMission> findByMissionName(String missionName);

    List<ExplorationMission> findByTargetPlanetId(Long planetId);

    List<ExplorationMission> findByStatus(MissionStatus status);

    List<ExplorationMission> findByLaunchDateBetween(LocalDate startDate, LocalDate endDate);
}
