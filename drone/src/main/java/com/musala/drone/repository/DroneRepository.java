package com.musala.drone.repository;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

import com.musala.drone.model.entity.Drone;
import com.musala.drone.model.enums.DroneState;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneRepository extends JpaRepository<Drone, String> {
    @Query("select d from Drone d where d.batteryCapacity >= 25 and d.state in (:availableStatesForLoading)")
    Collection<Drone> getAvailableDrones(Collection<DroneState> availableStatesForLoading);

    /**
     * Here we use pessimistic lock for drone, if we try load the same drone in another tread, it will be wait for end
     * this transaction, so we don't use 'fetch' data here, for get actual 'medications' state for each loading
     */
    @Lock(PESSIMISTIC_WRITE)
    @Query("select d from Drone d where d.serialNumber = :id")
    Optional<Drone> findByIdForUpdate(String id);

    @Query("select d from Drone d left join fetch d.medications where d.serialNumber = :id")
    Optional<Drone> findByIdWithMedications(String id);
}