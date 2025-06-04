package ru.edu.penzgtu.lab.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "planets")
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    //Тип планеты. Например "Земной тип", "Газовый гигант"
    @Column(name = "type", length = 100)
    private String type;

    @Column(name = "diameter_km", nullable = false)
    private Double diameter;

    @Column(name = "has_atmosphere", nullable = false)
    private Boolean hasAtmosphere;

    @Column(name = "star_system", nullable = false, length = 150)
    private String starSystem;

    //Дата открытия, может быть null если неизвестна
    @Column(name = "discovery_date")
    private LocalDate discoveryDate;

    // Поверхностная гравитация в g
    @Column(name = "surface_gravity_g", nullable = false)
    private Double surfaceGravity;

    //Общее число спутников
    @Column(name = "number_of_moons_confirmed", nullable = false)
    private Integer numberOfMoonsConfirmed;

    //Связь со внешней таблицы спутников.
    //Отношение один ко многим, одна планета может иметь много спутников
    @OneToMany(
            mappedBy = "planet",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Sputnik> satellites = new ArrayList<>();

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
}
