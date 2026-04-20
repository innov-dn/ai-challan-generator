package com.challan.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;

@Data
@Document(collection = "challans")
public class Challan {

    @Id
    private String id;

    @Indexed
    private String vehicleNumber;
    private String ownerName;
    private String vehicleType;

    private String violationType;
    private String location;
    private String district;
    private String state;

    // AI Generated Fields
    private String challanDescription;
    private String motorVehiclesActSection;
    private Double fineAmount;
    private String penaltyDetails;
    private Boolean isCompoundable;
    private String aiSummary;

    // Challan Meta
    private String challanNumber;
    private LocalDateTime issuedAt;
    private String paymentDueDate;
    private String status;
}