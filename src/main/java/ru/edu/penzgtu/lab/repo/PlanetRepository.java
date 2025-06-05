package ru.edu.penzgtu.lab.repo;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edu.penzgtu.lab.entity.Planet;

import java.util.Optional;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {
    Optional<Planet> findByName(@NotBlank(message = "Название планеты не может быть пустым") String name);
}
