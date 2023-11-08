package com.musala.drone.mapper;

import com.musala.drone.model.entity.Audit;
import com.musala.drone.model.entity.Drone;
import java.time.Instant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = Instant.class)
public interface AuditMapper {
    @Mapping(target = "timestamp", expression = "java(Instant.now())")
    Audit map(Drone drone);
}
