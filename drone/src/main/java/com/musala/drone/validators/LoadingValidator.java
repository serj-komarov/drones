package com.musala.drone.validators;

import com.musala.drone.exception.BusinessException;
import com.musala.drone.model.entity.Drone;
import com.musala.drone.model.entity.Medication;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoadingValidator {
    private static final int MIN_BATTERY_CAPACITY = 25;

    public void validateLoading(Drone drone, List<Medication> medications) {
        if (drone.getBatteryCapacity() < MIN_BATTERY_CAPACITY) {
            throw BusinessException.builder().status(HttpStatus.BAD_REQUEST).message(
                String.format(
                    "Can't load to drone with serial number: '%s' battery level is '%s' percent! it's to low, try to use another drone",
                    drone.getSerialNumber(), drone.getBatteryCapacity())).build();
        }
        var alreadyLoadedWeight = drone.getMedications().stream().map(Medication::getWeight).reduce(0, Integer::sum);
        var pendingLoadWeight = medications.stream().map(Medication::getWeight).reduce(0, Integer::sum);

        var overWeight = pendingLoadWeight - (drone.getWeightLimit() - alreadyLoadedWeight);

        if (overWeight > 0) {
            throw BusinessException.builder().status(HttpStatus.BAD_REQUEST).message(
                String.format(
                    "Can't load to drone with serial number: '%s'! Overweight is '%s'. Try to use another drone or reduce weight",
                    drone.getSerialNumber(), overWeight)).build();
        }
    }


}
