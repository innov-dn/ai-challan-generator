package com.challan.service;

import com.challan.dto.ChallanRequestDTO;
import com.challan.model.Challan;
import com.challan.repository.ChallanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class ChallanService {

    @Autowired
    private ChallanRepository challanRepository;

    @Autowired
    private GeminiService geminiService;

    private String generateChallanNumber(String state) {
        String stateCode = state.substring(0, 2).toUpperCase();
        String year      = String.valueOf(LocalDateTime.now().getYear());
        long   count     = challanRepository.count() + 1;
        return String.format("CHN-%s-%s%04d", stateCode, year, count);
    }

    public Challan generate(ChallanRequestDTO req) throws Exception {

        // 1. Call Gemini AI
        Map<String, Object> aiData = geminiService.generateChallanData(
                req.getVehicleNumber().toUpperCase(),
                req.getVehicleType(),
                req.getViolationType(),
                req.getLocation(),
                req.getState()
        );

        // 2. Build Challan object
        Challan challan = new Challan();

        // From user input
        challan.setChallanNumber(generateChallanNumber(req.getState()));
        challan.setVehicleNumber(req.getVehicleNumber().toUpperCase());
        challan.setOwnerName(req.getOwnerName());
        challan.setVehicleType(req.getVehicleType());
        challan.setViolationType(req.getViolationType());
        challan.setLocation(req.getLocation());
        challan.setDistrict(req.getDistrict());
        challan.setState(req.getState());

        // From Gemini AI
        challan.setChallanDescription(
                (String) aiData.get("challanDescription"));
        challan.setMotorVehiclesActSection(
                (String) aiData.get("motorVehiclesActSection"));
        challan.setFineAmount(
                Double.valueOf(aiData.get("fineAmount").toString()));
        challan.setPenaltyDetails(
                (String) aiData.get("penaltyDetails"));
        challan.setIsCompoundable(
                (Boolean) aiData.get("isCompoundable"));
        challan.setAiSummary(
                (String) aiData.get("aiSummary"));

        // Meta
        challan.setIssuedAt(LocalDateTime.now());
        challan.setPaymentDueDate(
                LocalDateTime.now().plusDays(30)
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        challan.setStatus("PENDING");

        // 3. Save to MongoDB
        return challanRepository.save(challan);
    }

    public List<Challan> getAll() {
        return challanRepository.findAll();
    }

    public Challan getById(String id) {
        return challanRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Challan not found: " + id));
    }

    public List<Challan> getByVehicle(String vehicleNumber) {
        return challanRepository
                .findByVehicleNumber(vehicleNumber.toUpperCase());
    }

    public Challan updateStatus(String id, String status) {
        Challan c = getById(id);
        c.setStatus(status.toUpperCase());
        return challanRepository.save(c);
    }
}