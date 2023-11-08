package com.musala.drone.service;

import static com.musala.drone.util.Constants.MAX_FLEET_COUNT;

import com.musala.drone.exception.BusinessException;
import com.musala.drone.mapper.DroneMapper;
import com.musala.drone.model.dto.DroneDto;
import com.musala.drone.model.dto.NewDroneRequest;
import com.musala.drone.model.entity.Drone;
import com.musala.drone.model.entity.Medication;
import com.musala.drone.model.enums.DroneState;
import com.musala.drone.repository.DroneRepository;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DroneService {
    private final DroneRepository droneRepository;
    private final DroneMapper droneMapper;
    public static final String NOT_FOUND_DRONE_MESSAGE = "Drone with serial number: %s not found";
    private static final Set<DroneState> availableForLoadingStateSet = new HashSet<>(
        Set.of(DroneState.IDLE, DroneState.LOADING));
    private static final Set<DroneState> busyForLoadingStateSet = new HashSet<>(
        Set.of(DroneState.RETURNING, DroneState.LOADED, DroneState.DELIVERED, DroneState.DELIVERING));

    public DroneDto getDroneInfo(String id) {
        return droneMapper.map(getDroneById(id));
    }

    public Drone getDroneByIdFetchMedications(String id) {
        return droneRepository.findByIdWithMedications(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
            String.format(NOT_FOUND_DRONE_MESSAGE, id)));
    }

    public Drone getDroneById(String id) {
        return droneRepository.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
            String.format(NOT_FOUND_DRONE_MESSAGE, id)));
    }

    public Drone getDroneByIdForLoading(String id) {
        return droneRepository.findByIdForUpdate(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
            String.format(NOT_FOUND_DRONE_MESSAGE, id)));
    }

    public Drone startLoadingDrone(String id) {
        var drone = getDroneByIdForLoading(id);
        if (busyForLoadingStateSet.contains(drone.getState())) {
            throw BusinessException.builder().status(HttpStatus.BAD_REQUEST).message(
                String.format(
                    "Drone with serial number: '%s', state: '%s', not ready for loading now, try another drone",
                    drone.getSerialNumber(), drone.getState())).build();
        } else {
            if (drone.getState().equals(DroneState.IDLE)) {
                drone.setState(DroneState.LOADING);
            }
            return drone;
        }
    }

    public DroneDto register(NewDroneRequest newDrone) {
        couldBeAddedCheck(newDrone);
        var entity = droneMapper.map(newDrone);
        return droneMapper.map(droneRepository.save(entity));
    }

    public List<DroneDto> getAvailableDrones() {
        return droneRepository.getAvailableDrones(availableForLoadingStateSet)
            .stream()
            .map(droneMapper::map)
            .collect(Collectors.toList());
    }

    public Drone save(Drone drone) {
        return droneRepository.save(drone);
    }

    /**
     * If the drone loaded more than 95%, we transfer it to LOADED status
     */
    public void stopLoading(Drone drone) {
        var loadedWeight = drone.getMedications().stream().map(Medication::getWeight).reduce(0, Integer::sum);
        if ((drone.getWeightLimit() * 0.95) - loadedWeight <= 0) {
            drone.setState(DroneState.LOADED);
        }
    }

    @Transactional(readOnly = true)
    public Collection<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

    public void couldBeAddedCheck(NewDroneRequest newDrone) {
        if (droneRepository.count() >= MAX_FLEET_COUNT) {
            throw BusinessException.builder().status(HttpStatus.BAD_REQUEST).message(
                String.format("The limit of the number of drones(%s) has been reached", MAX_FLEET_COUNT)).build();
        }
        var existsDrone = droneRepository.existsById(newDrone.getSerialNumber());
        if (existsDrone) {
            throw BusinessException.builder().status(HttpStatus.BAD_REQUEST).message(
                String.format("Drone with serial number: '%s' already registered", newDrone.getSerialNumber())).build();
        }
    }
}
