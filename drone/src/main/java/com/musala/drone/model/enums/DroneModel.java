package com.musala.drone.model.enums;

import lombok.Getter;

@Getter
public enum DroneModel {
    LIGHT_WEIGHT(100),
    MIDDLE_WEIGHT(300),
    CRUISER_WEIGHT(400),
    HEAVY_WEIGHT(500);

    private final Integer maxWeight;

    DroneModel(Integer maxWeight) {
        this.maxWeight = maxWeight;
    }


}
