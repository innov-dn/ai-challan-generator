package com.challan.controller;

import com.challan.dto.ChallanRequestDTO;
import com.challan.model.Challan;
import com.challan.service.ChallanService;
import com.challan.service.PdfService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/challan")
public class ChallanController {

    @Autowired private ChallanService challanService;
    @Autowired private PdfService     pdfService;

    // POST /api/challan/generate
    @PostMapping("/generate")
    public ResponseEntity<?> generate(
            @Valid @RequestBody ChallanRequestDTO request) {
        try {
            Challan challan = challanService.generate(request);
            return ResponseEntity.ok(challan);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // GET /api/challan/all
    @GetMapping("/all")
    public ResponseEntity<List<Challan>> getAll() {
        return ResponseEntity.ok(challanService.getAll());
    }

    // GET /api/challan/vehicle/{vehicleNumber}
    @GetMapping("/vehicle/{vehicleNumber}")
    public ResponseEntity<List<Challan>> getByVehicle(
            @PathVariable String vehicleNumber) {
        return ResponseEntity.ok(
                challanService.getByVehicle(vehicleNumber));
    }

    // PUT /api/challan/{id}/status?status=PAID
    @PutMapping("/{id}/status")
    public ResponseEntity<Challan> updateStatus(
            @PathVariable String id,
            @RequestParam String status) {
        return ResponseEntity.ok(
                challanService.updateStatus(id, status));
    }

    // GET /api/challan/{id}/pdf
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable String id) {
        try {
            Challan challan  = challanService.getById(id);
            byte[]  pdfBytes = pdfService.generateChallanPdf(challan);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=challan-"
                                    + challan.getChallanNumber() + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}