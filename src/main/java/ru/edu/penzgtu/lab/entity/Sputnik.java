package ru.edu.penzgtu.lab.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "planet")
@Entity
@Table(name = "sputniks")
public class Sputnik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "orbital_period_days")
    private Double orbitalPeriod;

    @Column(name = "is_natural")
    private Boolean isNatural; // Естественный или искусственный спутник

    // Отношение многие к одному. много спутников могут принадлежать одной планете
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planet_id", nullable = false) // Внешний ключ в таблице 'sputniks', ссылается на 'id' из 'planets'
    @JsonBackReference
    private Planet planet;
}
