package com.musala.drone.mapper;

import com.musala.drone.model.dto.DroneMedicationDto;
import com.musala.drone.model.dto.MedicationDto;
import com.musala.drone.model.entity.Drone;
import com.musala.drone.model.entity.Medication;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DroneMedicationMapper {
    @Named("mapDroneMaxWeight")
    static Integer mapDroneMaxWeight(List<Medication> medications) {
        return medications.stream().map(Medication::getWeight).reduce(0, Integer::sum);
    }

    @Named("mapMedications")
    static List<MedicationDto> mapMedications(List<Medication> medications) {
        return medications.stream()
            .map(o -> MedicationDto.builder().name(o.getName()).code(o.getCode()).weight(o.getWeight()).build())
            .toList();
    }

    @Mapping(target = "droneSerialNumber", source = "serialNumber")
    @Mapping(target = "droneMaxWeight", source = "weightLimit")
    @Mapping(target = "loadedWeight", source = "medications", qualifiedByName = "mapDroneMaxWeight")
    @Mapping(target = "medications", source = "medications", qualifiedByName = "mapMedications")
    DroneMedicationDto map(Drone drone);

}
