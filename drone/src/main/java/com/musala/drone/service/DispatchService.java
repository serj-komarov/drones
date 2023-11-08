package com.musala.drone.service;

import com.musala.drone.exception.BusinessException;
import com.musala.drone.mapper.DroneMedicationMapper;
import com.musala.drone.model.dto.DroneDto;
import com.musala.drone.model.dto.DroneMedicationDto;
import com.musala.drone.model.dto.LoadRequest;
import com.musala.drone.model.dto.NewDroneRequest;
import com.musala.drone.validators.LoadingValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class DispatchService {
    private final LoadingValidator loadingValidator;
    private final DroneService droneService;
    private final MedicationService medicationService;
    private final DroneMedicationMapper droneMedicationMapper;

    @Transactional
    public DroneDto register(NewDroneRequest newDrone) {
        return droneService.register(newDrone);
    }

    @Transactional(readOnly = true)
    public DroneDto getDroneInfo(String id) {
        return droneService.getDroneInfo(id);
    }

    @Transactional(readOnly = true)
    public DroneMedicationDto getDroneMedication(String id) {
        var drone = droneService.getDroneByIdFetchMedications(id);
        return droneMedicationMapper.map(drone);
    }

    @Transactional(readOnly = true)
    public List<DroneDto> getAvailableDrones() {
        var drones = droneService.getAvailableDrones();
        if (CollectionUtils.isEmpty(drones)) {
            throw BusinessException.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("Available drones for loading not found")
                .build();
        } else {
            return drones;
        }
    }

    @Transactional
    public DroneMedicationDto load(String id, LoadRequest loadRequest) {
        var drone = droneService.startLoadingDrone(id);
        var medications = medicationService.getMedicationsByCodes(loadRequest.getCodes());
        loadingValidator.validateLoading(drone, medications);
        drone.getMedications().addAll(medications);
        droneService.stopLoading(drone);
        drone = droneService.save(drone);
        return droneMedicationMapper.map(drone);
    }
}
