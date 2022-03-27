package com.futureSheep.ApplicationMS_kbe.controller;

import com.futureSheep.ApplicationMS_kbe.services.CSVExporter;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CommonsLog
@RestController
@RequestMapping("/api/v1")
public class CSVExporterController {


    private final CSVExporter csvExporter;

    public CSVExporterController(CSVExporter csvExporter) {
        this.csvExporter = csvExporter;
    }


    @Operation(summary = "Download CSV", description = "Download CSV-File with all Laptops in the datastorage", tags ="CSV-Exporter")
    @RequestMapping(path = "/laptops/csv")
    public void getAllLaptopsInCsv(HttpServletResponse servletResponse) {
        try {
            log.info("GET Request for csv file /laptops/csv");
            servletResponse.setContentType("text/csv");
            servletResponse.addHeader("Content-Disposition","attachment; filename=\"laptops.csv\"");
            csvExporter.writeLaptopsToCSV(servletResponse.getWriter());
        } catch (IOException e) {
            log.error("Error while Request at /laptops/csv " + e);
        }

    }


}
