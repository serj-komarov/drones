package com.musala.drone.validators;

import com.musala.drone.model.enums.DroneModel;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class CustomEnumTypeValidator implements ConstraintValidator<CustomEnumType, DroneModel> {
    private DroneModel[] subset;

    @Override
    public void initialize(CustomEnumType constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(DroneModel value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}
