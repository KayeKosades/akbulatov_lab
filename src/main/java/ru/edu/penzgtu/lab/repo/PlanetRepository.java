package ru.edu.penzgtu.lab.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edu.penzgtu.lab.entity.Planet;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {
}
