package ru.edu.penzgtu.lab.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.edu.penzgtu.lab.entity.enums.MissionStatus;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "targetPlanet")
@Entity
@Table(name = "exploration_missions")
public class ExplorationMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название миссии не может быть пустым")
    @Column(name = "mission_name", nullable = false, unique = true)
    private String missionName;

    @NotNull(message = "Дата запуска миссии не может быть пустой")
    @PastOrPresent(message = "Дата запуска миссии не может быть в будущем")
    @Column(name = "launch_date", nullable = false)
    private LocalDate launchDate;

    @FutureOrPresent(message = "Дата завершения миссии не может быть в прошлом (если указана)")
    @Column(name = "completion_date") // Может быть null, если миссия еще активна
    private LocalDate completionDate;

    @Column(name = "objective", columnDefinition = "TEXT") // Цель миссии
    private String objective;

    @Enumerated(EnumType.STRING) // Статус миссии
    @Column(name = "status")
    private MissionStatus status; // Enum для статуса (например, PLANNED, ACTIVE, COMPLETED, FAILED)

    //Много ExplorationMission могут быть нацелены на одну Planet
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planet_id", nullable = false) // Внешний ключ, миссия должна иметь целевую планету
    @NotNull(message = "Исследовательская миссия должна быть связана с планетой")
    @JsonBackReference(value="planet-missions") // Для корректной сериализации JSON
    private Planet targetPlanet;
}
