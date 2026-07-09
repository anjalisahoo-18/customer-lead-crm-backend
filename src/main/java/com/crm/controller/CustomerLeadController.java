package com.crm.controller;

import com.crm.model.CustomerLead;
import com.crm.model.LeadType;
import com.crm.service.CustomerLeadService;
import com.crm.service.LeadTypeService;
import com.crm.repository.LeadTypeRepository;
import com.crm.exception.BadRequestException;
import com.crm.exception.ResourceNotFoundException;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/leads")
public class CustomerLeadController {

    @Autowired
    private CustomerLeadService leadService;

    @Autowired
    private LeadTypeRepository leadTypeRepository;

    @GetMapping
    public ResponseEntity<Page<CustomerLead>> getLeads(
            @RequestParam(required = false) Long leadTypeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String assignedExecutive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate,desc") String[] sort) {

        List<Sort.Order> orders = new ArrayList<>();
        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(Sort.Direction.fromString(_sort[1]), _sort[0]));
            }
        } else {
            orders.add(new Sort.Order(Sort.Direction.fromString(sort[1]), sort[0]));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        return ResponseEntity.ok(leadService.getFilteredLeads(
                leadTypeId, status, priority, city, startDate, endDate, customerName, mobile, search, assignedExecutive, pageable
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerLead> getLeadById(@PathVariable Long id) {
        return ResponseEntity.ok(leadService.getLeadById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerLead> createLead(@RequestBody CustomerLead lead) {
        return ResponseEntity.ok(leadService.createLead(lead));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerLead> updateLead(@PathVariable Long id, @RequestBody CustomerLead lead) {
        return ResponseEntity.ok(leadService.updateLead(id, lead));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLead(@PathVariable Long id) {
        leadService.deleteLead(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reminders")
    public ResponseEntity<List<CustomerLead>> getReminders(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate reminderDate = date != null ? date : LocalDate.now();
        return ResponseEntity.ok(leadService.getReminders(reminderDate));
    }

    @GetMapping("/export/excel")
    public void exportToExcel(
            @RequestParam(required = false) Long leadTypeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String assignedExecutive,
            HttpServletResponse response) throws IOException {

        List<CustomerLead> leads = leadService.getFilteredLeadsList(
                leadTypeId, status, priority, city, startDate, endDate, customerName, mobile, search, assignedExecutive
        );

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Customer Leads");

        // Headers
        String[] columns = {"ID", "Customer Name", "Mobile", "Alternate Number", "Email", "Lead Type", "City", "Address", "Requirement", "Lead Source", "Assigned Executive", "Discussion Details", "Visit Date", "Next Follow-up Date", "Status", "Priority", "Created Date"};
        
        Row headerRow = sheet.createRow(0);
        CellStyle headerCellStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerCellStyle.setFont(headerFont);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Fill data
        int rowNum = 1;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (CustomerLead lead : leads) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(lead.getId());
            row.createCell(1).setCellValue(lead.getCustomerName());
            row.createCell(2).setCellValue(lead.getMobile());
            row.createCell(3).setCellValue(lead.getAlternateNumber() != null ? lead.getAlternateNumber() : "");
            row.createCell(4).setCellValue(lead.getEmail() != null ? lead.getEmail() : "");
            row.createCell(5).setCellValue(lead.getLeadType() != null ? lead.getLeadType().getName() : "");
            row.createCell(6).setCellValue(lead.getCity() != null ? lead.getCity() : "");
            row.createCell(7).setCellValue(lead.getAddress() != null ? lead.getAddress() : "");
            row.createCell(8).setCellValue(lead.getRequirement() != null ? lead.getRequirement() : "");
            row.createCell(9).setCellValue(lead.getLeadSource() != null ? lead.getLeadSource() : "");
            row.createCell(10).setCellValue(lead.getAssignedExecutive() != null ? lead.getAssignedExecutive() : "");
            row.createCell(11).setCellValue(lead.getDiscussionDetails() != null ? lead.getDiscussionDetails() : "");
            row.createCell(12).setCellValue(lead.getVisitDate() != null ? lead.getVisitDate().format(dateFormatter) : "");
            row.createCell(13).setCellValue(lead.getNextFollowupDate() != null ? lead.getNextFollowupDate().format(dateFormatter) : "");
            row.createCell(14).setCellValue(lead.getStatus());
            row.createCell(15).setCellValue(lead.getPriority());
            row.createCell(16).setCellValue(lead.getCreatedDate() != null ? lead.getCreatedDate().format(dateTimeFormatter) : "");
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=leads_export.xlsx");
        
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/export/pdf")
    public void exportToPdf(
            @RequestParam(required = false) Long leadTypeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String assignedExecutive,
            HttpServletResponse response) throws IOException {

        List<CustomerLead> leads = leadService.getFilteredLeadsList(
                leadTypeId, status, priority, city, startDate, endDate, customerName, mobile, search, assignedExecutive
        );

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=leads_export.pdf");

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        
        // Title
        com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Customer Leads Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Table
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.5f, 1.5f, 1.8f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f});

        // Headers
        com.lowagie.text.Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        String[] headers = {"Name", "Mobile", "Lead Type", "City", "Source", "Status", "Priority", "Next Follow-up"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
            table.addCell(cell);
        }

        // Data Rows
        com.lowagie.text.Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (CustomerLead lead : leads) {
            table.addCell(new Phrase(lead.getCustomerName(), dataFont));
            table.addCell(new Phrase(lead.getMobile(), dataFont));
            table.addCell(new Phrase(lead.getLeadType() != null ? lead.getLeadType().getName() : "", dataFont));
            table.addCell(new Phrase(lead.getCity() != null ? lead.getCity() : "", dataFont));
            table.addCell(new Phrase(lead.getLeadSource() != null ? lead.getLeadSource() : "", dataFont));
            table.addCell(new Phrase(lead.getStatus(), dataFont));
            table.addCell(new Phrase(lead.getPriority(), dataFont));
            table.addCell(new Phrase(lead.getNextFollowupDate() != null ? lead.getNextFollowupDate().format(dateFormatter) : "", dataFont));
        }

        document.add(table);
        document.close();
    }

    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importCSV(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Uploaded file is empty");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int rowCount = 0;
            int successCount = 0;
            int errorCount = 0;
            List<String> errors = new ArrayList<>();
            List<CustomerLead> leadsToSave = new ArrayList<>();

            // Read header
            String header = br.readLine();
            // Expected columns: Customer Name, Mobile, Alternate Number, Email, Lead Type, City, Address, Requirement, Lead Source, Assigned Executive, Status, Priority

            while ((line = br.readLine()) != null) {
                rowCount++;
                // Simple CSV parse (handling values optionally enclosed in quotes)
                String[] values = parseCsvLine(line);
                if (values.length < 2) {
                    errorCount++;
                    errors.add("Row " + rowCount + ": Incomplete fields");
                    continue;
                }

                try {
                    CustomerLead lead = new CustomerLead();
                    lead.setCustomerName(values[0].trim());
                    lead.setMobile(values[1].trim());
                    
                    if (values.length > 2) lead.setAlternateNumber(values[2].trim());
                    if (values.length > 3) lead.setEmail(values[3].trim());
                    
                    // Lead Type
                    String leadTypeName = values.length > 4 ? values[4].trim() : "Other";
                    if (leadTypeName.isEmpty()) leadTypeName = "Other";
                    
                    final String finalLeadTypeName = leadTypeName;
                    LeadType leadType = leadTypeRepository.findByName(finalLeadTypeName)
                            .orElseGet(() -> leadTypeRepository.save(new LeadType(finalLeadTypeName, "Auto-created on CSV Import")));
                    lead.setLeadType(leadType);

                    if (values.length > 5) lead.setCity(values[5].trim());
                    if (values.length > 6) lead.setAddress(values[6].trim());
                    if (values.length > 7) lead.setRequirement(values[7].trim());
                    if (values.length > 8) lead.setLeadSource(values[8].trim());
                    if (values.length > 9) lead.setAssignedExecutive(values[9].trim());
                    
                    // Status & Priority
                    lead.setStatus(values.length > 10 && !values[10].isEmpty() ? values[10].trim() : "New");
                    lead.setPriority(values.length > 11 && !values[11].isEmpty() ? values[11].trim() : "Warm");
                    
                    lead.setCreatedDate(LocalDateTime.now());
                    
                    leadsToSave.add(lead);
                    successCount++;
                } catch (Exception ex) {
                    errorCount++;
                    errors.add("Row " + rowCount + ": " + ex.getMessage());
                }
            }

            leadService.saveAll(leadsToSave);

            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("total", rowCount);
            result.put("success", successCount);
            result.put("errorsCount", errorCount);
            result.put("errors", errors);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new BadRequestException("Failed to parse CSV file: " + e.getMessage());
        }
    }

    private String[] parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(sb.toString().replaceAll("^\"|\"$", ""));
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        values.add(sb.toString().replaceAll("^\"|\"$", ""));
        return values.toArray(new String[0]);
    }
}
