package com.musala.drone.controller;

import static com.musala.drone.util.Constants.DISPATCHER_API;

import com.musala.drone.model.dto.DroneDto;
import com.musala.drone.model.dto.DroneMedicationDto;
import com.musala.drone.model.dto.LoadRequest;
import com.musala.drone.model.dto.NewDroneRequest;
import com.musala.drone.service.DispatchService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = DISPATCHER_API)
@RequiredArgsConstructor
public class DispatchController {
    private final DispatchService dispatchService;

    @PostMapping("/drone/register")
    public ResponseEntity<DroneDto> registerDrone(@RequestBody @Valid NewDroneRequest newDrone) {
        return ResponseEntity.ok(dispatchService.register(newDrone));
    }

    @PatchMapping("/drone/load/{id}")
    public ResponseEntity<DroneMedicationDto> loadDrone(@PathVariable String id,
        @RequestBody @Valid LoadRequest loadRequest) {
        return ResponseEntity.ok(dispatchService.load(id, loadRequest));
    }

    @GetMapping("/check/drone/available-for-loading")
    public ResponseEntity<List<DroneDto>> getAvailableDrones() {
        return ResponseEntity.ok(dispatchService.getAvailableDrones());
    }

    @GetMapping("/check/drone/{id}")
    public ResponseEntity<DroneDto> getDroneInfo(@PathVariable String id) {
        return ResponseEntity.ok(dispatchService.getDroneInfo(id));
    }

    @GetMapping("/check/drone/medication/{id}")
    public ResponseEntity<DroneMedicationDto> getDroneMedication(@PathVariable String id) {
        return ResponseEntity.ok(dispatchService.getDroneMedication(id));
    }

}
