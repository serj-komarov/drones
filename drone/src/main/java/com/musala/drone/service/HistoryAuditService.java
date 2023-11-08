package com.musala.drone.service;

import com.musala.drone.mapper.AuditMapper;
import com.musala.drone.repository.AuditRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryAuditService {
    private final DroneService droneService;
    private final AuditMapper auditMapper;
    private final AuditRepository auditRepository;

    @Scheduled(cron = "${drone.audit.scheduler.cron}")
    public void droneAudit() {
        start();
    }

    /**
     * In this test assignment, I have chosen to store audit logs in a relational database due to its simplicity,
     * familiarity, and suitability for the structured data of Drone entities. For larger-scale, production
     * environments, a more specialized solution like Elasticsearch may be preferable for its advanced querying and
     * scalability. However, for the scope of this assignment, the relational database provides a practical and
     * efficient storage solution.
     */
    private void start() {
        auditRepository.saveAll(
            droneService.getAllDrones().stream().map(auditMapper::map).collect(Collectors.toList()));
    }

}
