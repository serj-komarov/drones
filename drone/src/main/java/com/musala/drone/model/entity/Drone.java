package com.musala.drone.model.entity;

import static com.musala.drone.util.Constants.MAX_CAPACITY;
import static com.musala.drone.util.Constants.MAX_WEIGHT;

import com.musala.drone.model.enums.DroneModel;
import com.musala.drone.model.enums.DroneState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "drone")
public class Drone {

    @Id
    @Column(name = "serial_number", nullable = false, length = 100)
    private String serialNumber;
    @Positive
    @Max(MAX_WEIGHT)
    @Column(name = "weight_limit", nullable = false)
    private Integer weightLimit;

    @PositiveOrZero
    @Max(MAX_CAPACITY)
    @Column(name = "battery_capacity", nullable = false)
    private Integer batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "model", nullable = false)
    private DroneModel model;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private DroneState state;
    @ManyToMany
    @Builder.Default
    @JoinTable(name = "drone_medication",
        joinColumns = @JoinColumn(name = "drone_id"),
        inverseJoinColumns = @JoinColumn(name = "medication_id"))
    private List<Medication> medications = new ArrayList<>();
}
