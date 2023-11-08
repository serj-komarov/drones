package com.musala.drone.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.musala.drone.model.enums.DroneModel;
import com.musala.drone.model.enums.DroneState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DroneDto {
    private String serialNumber;
    private Integer weightLimit;
    private Integer batteryCapacity;
    private DroneModel model;
    private DroneState state;
}
