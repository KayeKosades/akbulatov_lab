package ru.edu.penzgtu.lab.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "diameter_km")
    private Double diameter;

    @Column(name = "has_atmosphere")
    private Boolean hasAtmosphere;

    @Column(name = "star_system")
    private String starSystem;

    // Отношение один ко многим, одна планета может иметь много спутников
    @OneToMany(
            mappedBy = "planet",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Sputnik> satellites = new ArrayList<>();
}
