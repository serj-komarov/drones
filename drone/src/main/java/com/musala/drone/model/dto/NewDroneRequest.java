package com.musala.drone.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.musala.drone.model.enums.DroneModel;
import com.musala.drone.validators.CustomEnumType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewDroneRequest {
    @Length(max = 100, message = "field 'serialNumber' must be less 100 characters")
    @NotNull(message = "field 'serialNumber' must be present")
    private String serialNumber;

    @CustomEnumType(anyOf = {DroneModel.HEAVY_WEIGHT, DroneModel.LIGHT_WEIGHT, DroneModel.MIDDLE_WEIGHT,
        DroneModel.CRUISER_WEIGHT}, message = "field 'model' must be any of {anyOf}")
    private DroneModel model;
}
