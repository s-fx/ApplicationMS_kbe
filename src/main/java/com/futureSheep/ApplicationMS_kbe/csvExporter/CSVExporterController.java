package com.futureSheep.ApplicationMS_kbe.csvExporter;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CommonsLog
@RestController
@RequestMapping("/api")
public class CSVExporterController {


    @Autowired
    private CSVExporter csvExporter;


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
