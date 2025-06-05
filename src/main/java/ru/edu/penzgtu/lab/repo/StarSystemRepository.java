package ru.edu.penzgtu.lab.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edu.penzgtu.lab.entity.StarSystem;

import java.util.Optional;

@Repository
public interface StarSystemRepository extends JpaRepository<StarSystem, Long> {
    Optional<StarSystem> findByName(String name); // Поиск по имени, если нужно
}

