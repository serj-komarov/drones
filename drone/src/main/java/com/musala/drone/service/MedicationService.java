package com.musala.drone.service;

import com.musala.drone.exception.BusinessException;
import com.musala.drone.model.entity.Medication;
import com.musala.drone.repository.MedicationRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicationService {
    private final MedicationRepository medicationRepository;

    public List<Medication> getMedicationsByCodes(List<String> codes) {
        var medications = medicationRepository.findAllById(codes);
        if (codes.size() != medications.size()) {
            var notFoundCodes = findNotFoundCodes(codes, medications);
            throw BusinessException.builder().status(HttpStatus.NOT_FOUND).message(
                String.format("Medications with codes: %s not found", notFoundCodes)).build();
        } else {
            return medications;
        }
    }
    private Set<String> findNotFoundCodes(List<String> codes, List<Medication> medications) {
        var foundCodes = medications.stream().map(Medication::getCode).collect(Collectors.toSet());
        return codes.stream().filter(o -> !foundCodes.contains(o)).collect(Collectors.toSet());
    }
}
