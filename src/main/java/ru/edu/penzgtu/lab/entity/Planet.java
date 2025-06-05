package ru.edu.penzgtu.lab.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"satellites", "starSystemEntity", "explorationMissions"})
@ToString(exclude = {"satellites", "starSystemEntity", "explorationMissions"})
@Entity
@Table(name = "planets")
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Название планеты не может быть null")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "type")
    private String type;

    @NotNull(message = "Диаметр не может быть null")
    @Positive(message = "Диаметр должен быть положительным")
    @Column(name = "diameter_km", nullable = false)
    private Double diameter;

    @Column(name = "has_atmosphere")
    private Boolean hasAtmosphere;


    @PastOrPresent(message = "Дата открытия не может быть в будущем")
    @Column(name = "discovery_date")
    private LocalDate discoveryDate;

    @Positive(message = "Поверхностная гравитация должна быть положительной")
    @Column(name = "surface_gravity")
    private Double surfaceGravity;

    @NotNull(message = "Количество спутников не может быть null")
    @Min(value = 0, message = "Количество спутников не может быть отрицательным")
    @Column(name = "number_of_moons", nullable = false)
    private Integer numberOfMoons;

    @ManyToOne(fetch = FetchType.LAZY)              //Много планет может относиться к одной звёдной системе
    @JoinColumn(name = "star_system_id")            //Поле в Planet, которое ссылается на StarSystem
    @JsonBackReference(value="planet-starSystem")
    private StarSystem starSystemEntity;

    @OneToMany(
            mappedBy = "targetPlanet",      // Поле в ExplorationMission, которое ссылается на Planet
            cascade = CascadeType.ALL,      // Если удаляем Planet, удаляются все ее ExplorationMission
            orphanRemoval = true,           // Если миссия удаляется из списка, она удаляется из БД
            fetch = FetchType.LAZY
    )
    @JsonManagedReference(value="planet-missions") // Для корректной сериализации JSON
    private List<ExplorationMission> explorationMissions = new ArrayList<>();

    @OneToMany(
            mappedBy = "planet",            // Поле в Sputnik, которое ссылается на Planet
            cascade = CascadeType.ALL,      // Если удаляем Planet, удаляются все ее осиротевшие спутники
            orphanRemoval = true,           // Если спутник удаляется из списка, он удаляется из БД
            fetch = FetchType.LAZY
    )
    @JsonManagedReference(value="planet-satellites")
    private List<Sputnik> satellites = new ArrayList<>();

    // Вспомогательные методы
    public void addSputnik(Sputnik sputnik) {
        if (sputnik != null) {
            this.satellites.add(sputnik);
            sputnik.setPlanet(this);
        }
    }

    public void removeSputnik(Sputnik sputnik) {
        if (sputnik != null) {
            this.satellites.remove(sputnik);
            sputnik.setPlanet(null);
        }
    }

    public void addExplorationMission(ExplorationMission mission) {
        if (mission != null) {
            this.explorationMissions.add(mission);
            mission.setTargetPlanet(this);
        }
    }

    public void removeExplorationMission(ExplorationMission mission) {
        if (mission != null) {
            this.explorationMissions.remove(mission);
            mission.setTargetPlanet(null);
        }
    }
}
