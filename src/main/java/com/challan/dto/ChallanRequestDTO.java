package com.challan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChallanRequestDTO {

    @NotBlank(message = "Vehicle number is required")
    @Pattern(
            regexp = "^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$",
            message = "Invalid format. Use: MP04AB1234"
    )
    private String vehicleNumber;

    @NotBlank(message = "Owner name is required")
    private String ownerName;

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;

    @NotBlank(message = "Violation type is required")
    private String violationType;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "District is required")
    private String district;

    @NotBlank(message = "State is required")
    private String state;
}