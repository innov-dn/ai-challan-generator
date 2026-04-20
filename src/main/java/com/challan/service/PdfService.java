package com.challan.service;

import com.challan.model.Challan;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    private static final DeviceRgb INDIA_ORANGE = new DeviceRgb(255, 153, 51);
    private static final DeviceRgb INDIA_GREEN  = new DeviceRgb(19, 136, 8);
    private static final DeviceRgb NAVY_BLUE    = new DeviceRgb(0, 0, 128);

    public byte[] generateChallanPdf(Challan challan) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer           = new PdfWriter(baos);
        PdfDocument pdf            = new PdfDocument(writer);
        Document doc               = new Document(pdf);

        // Top orange stripe
        Table topStripe = new Table(1).useAllAvailableWidth();
        topStripe.addCell(new Cell()
                .add(new Paragraph(" ").setFontSize(4))
                .setBorder(null)
                .setBackgroundColor(INDIA_ORANGE));
        doc.add(topStripe);

        // Header
        doc.add(new Paragraph("GOVERNMENT OF INDIA")
                .setBold().setFontSize(14)
                .setFontColor(NAVY_BLUE)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(10));

        doc.add(new Paragraph("TRAFFIC POLICE DEPARTMENT")
                .setFontSize(11)
                .setFontColor(NAVY_BLUE)
                .setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph("MOTOR VEHICLES ACT, 1988 (AMENDED 2019)")
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY));

        doc.add(new Paragraph("CHALLAN / NOTICE OF TRAFFIC OFFENCE")
                .setBold().setFontSize(16)
                .setFontColor(ColorConstants.RED)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(8)
                .setMarginBottom(15));

        // Details table
        Table table = new Table(
                UnitValue.createPercentArray(new float[]{40, 60}))
                .useAllAvailableWidth()
                .setMarginBottom(15);

        addRow(table, "Challan Number",  challan.getChallanNumber());
        addRow(table, "Vehicle Number",  challan.getVehicleNumber());
        addRow(table, "Owner / Driver",  challan.getOwnerName());
        addRow(table, "Vehicle Type",    challan.getVehicleType());
        addRow(table, "Violation",       challan.getViolationType());
        addRow(table, "MV Act Section",  challan.getMotorVehiclesActSection());
        addRow(table, "Fine Amount",     "Rs. " + challan.getFineAmount());
        addRow(table, "Location",        challan.getLocation());
        addRow(table, "District",        challan.getDistrict());
        addRow(table, "State",           challan.getState());
        addRow(table, "Issued On",
                challan.getIssuedAt() != null
                        ? challan.getIssuedAt().toLocalDate().toString()
                        : "N/A");
        addRow(table, "Payment Due By",  challan.getPaymentDueDate());
        addRow(table, "Status",          challan.getStatus());
        addRow(table, "Compoundable",
                Boolean.TRUE.equals(challan.getIsCompoundable())
                        ? "Yes" : "No");
        doc.add(table);

        // Description
        doc.add(new Paragraph("Offence Description:")
                .setBold().setFontSize(10).setFontColor(NAVY_BLUE));
        doc.add(new Paragraph(
                challan.getChallanDescription() != null
                        ? challan.getChallanDescription() : "N/A")
                .setFontSize(10).setMarginBottom(8));

        doc.add(new Paragraph("Penalty Details:")
                .setBold().setFontSize(10).setFontColor(NAVY_BLUE));
        doc.add(new Paragraph(
                challan.getPenaltyDetails() != null
                        ? challan.getPenaltyDetails() : "N/A")
                .setFontSize(10).setMarginBottom(8));

        // Footer
        doc.add(new Paragraph(
                "Pay online: https://echallan.parivahan.gov.in | Helpline: 112")
                .setFontSize(9)
                .setFontColor(ColorConstants.GRAY)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20));

        // Bottom green stripe
        Table bottomStripe = new Table(1).useAllAvailableWidth();
        bottomStripe.addCell(new Cell()
                .add(new Paragraph(" ").setFontSize(4))
                .setBorder(null)
                .setBackgroundColor(INDIA_GREEN));
        doc.add(bottomStripe);

        doc.close();
        return baos.toByteArray();
    }

    private void addRow(Table table, String key, String value) {
        table.addCell(new Cell()
                .add(new Paragraph(key).setBold().setFontSize(10))
                .setBackgroundColor(new DeviceRgb(240, 240, 255)));
        table.addCell(new Cell()
                .add(new Paragraph(
                        value != null ? value : "N/A").setFontSize(10)));
    }
}