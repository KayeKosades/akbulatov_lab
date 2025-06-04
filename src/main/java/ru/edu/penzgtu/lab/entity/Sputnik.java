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

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    //Период обращения вокруг планеты
    @Column(name = "orbital_period_days", nullable = false)
    private Double orbitalPeriod;

    //Естественный или искусственный спутник
    @Column(name = "is_natural", nullable = false)
    private Boolean isNatural;

    //Средний радиус в км
    @Column(name = "mean_radius_km", nullable = false)
    private Double meanRadiusKm;

    // Масса в 10^20 кг
    @Column(name = "mass_10_pow_20_kg")
    private Double massKg;

    //Связь с таблицей планет
    //Отношение многие к одному. много спутников могут принадлежать одной планете
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planet_id", nullable = false) // Внешний ключ в таблице 'sputniks', ссылается на 'id' из 'planets'
    @JsonBackReference
    private Planet planet;
}
