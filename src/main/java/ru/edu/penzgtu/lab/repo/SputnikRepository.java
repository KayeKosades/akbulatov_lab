package ru.edu.penzgtu.lab.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edu.penzgtu.lab.entity.Sputnik;

import java.util.List;

@Repository
public interface SputnikRepository extends JpaRepository<Sputnik, Long> {
    List<Sputnik> findByPlanetId(Long planetId);
}
