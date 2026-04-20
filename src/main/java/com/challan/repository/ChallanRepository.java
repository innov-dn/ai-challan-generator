package com.challan.repository;

import com.challan.model.Challan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChallanRepository
        extends MongoRepository<Challan, String> {

    List<Challan> findByVehicleNumber(String vehicleNumber);
    List<Challan> findByStatus(String status);
    List<Challan> findByState(String state);
}