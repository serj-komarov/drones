package com.musala.drone.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medication")
public class Medication {
    @Id
    @Pattern(
        regexp = "[A-Z0-9_]+",
        message = "allowed only upper case letters, underscore and numbers"
    )
    @Column(name = "code")
    private String code;

    @Column(nullable = false)
    @Pattern(
        regexp = "[a-zA-Z_0-9-]+",
        message = "allowed only letters, numbers, ‘-‘, ‘_’"
    )
    private String name;

    @Column(nullable = false)
    private Integer weight;

    /**
     * Storing images in a PostgreSQL database isn't the most efficient choice. It's generally better to store a
     * reference (URL) to the image and keep the actual image file on an Amazon S3 server. However, implementing this
     * isn't necessary for the current task, and it would be a time-consuming addition.
     */
    @Column
    private String image;
}