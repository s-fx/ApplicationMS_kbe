package com.futureSheep.ApplicationMS_kbe.csvExporter;

import com.futureSheep.ApplicationMS_kbe.productService.ProductService;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@CommonsLog
@Service
public class CSVExporterImpl implements CSVExporter {

    private final ProductService productService;

    public CSVExporterImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void writeLaptopsToCSV(Writer writer) {
        List<EntityModel<Laptop>> allLaptops = productService.collectAllLaptops();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            log.info("CSV Exporter started, gathering laptops...");
            writeEachLaptopIntoCSV(allLaptops,csvPrinter);
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }


    private void writeEachLaptopIntoCSV(List<EntityModel<Laptop>> allLaptops, CSVPrinter csvPrinter) throws IOException {
        for (EntityModel<Laptop> entityLaptop : allLaptops) {
            Laptop laptop = entityLaptop.getContent();
            assert laptop != null;
            csvPrinter.printRecord(laptop.getId(), laptop.getBrand(), laptop.getPrice(), laptop.getWeight());
        }
    }
}
