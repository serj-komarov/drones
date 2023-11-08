package com.musala.drone.mapper;

import com.musala.drone.model.dto.DroneDto;
import com.musala.drone.model.dto.NewDroneRequest;
import com.musala.drone.model.entity.Drone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface DroneMapper {

    @Mapping(target = "state", constant = "IDLE")
    @Mapping(target = "batteryCapacity", constant = "100")
    @Mapping(target = "weightLimit", expression = "java(newDrone.getModel().getMaxWeight())")
    Drone map(NewDroneRequest newDrone);

    DroneDto map(Drone drone);
}

