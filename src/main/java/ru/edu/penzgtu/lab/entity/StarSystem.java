package ru.edu.penzgtu.lab.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "planets")
@Entity
@Table(name = "star_systems")
public class StarSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название звездной системы не может быть пустым")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "galaxy_name")
    private String galaxyName;

    @PastOrPresent(message = "Дата открытия звездной системы не может быть в будущем")
    @Column(name = "discovery_date")
    private LocalDate discoveryDate;

    @OneToMany(
            mappedBy = "starSystemEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Planet> planets = new ArrayList<>();

    public void addPlanet(Planet planet) {
        if (planet != null) {
            this.planets.add(planet);
            planet.setStarSystemEntity(this);
        }
    }

    public void removePlanet(Planet planet) {
        if (planet != null) {
            this.planets.remove(planet);
            planet.setStarSystemEntity(null);
        }
    }
}
