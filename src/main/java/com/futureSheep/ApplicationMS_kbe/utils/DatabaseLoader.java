package com.futureSheep.ApplicationMS_kbe.utils;

import com.futureSheep.ApplicationMS_kbe.products.LaptopLocationOnly;
import com.futureSheep.ApplicationMS_kbe.repositories.LaptopRepository;
import com.futureSheep.ApplicationMS_kbe.products.Location;
import com.futureSheep.ApplicationMS_kbe.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;

import java.math.BigDecimal;
import java.util.UUID;


@Configuration
public class DatabaseLoader {

    private static final Logger log = LoggerFactory.getLogger(DatabaseLoader.class);
    private final ProductService productService;

    public DatabaseLoader(ProductService productService) {
        this.productService = productService;
    }


    @Bean
    CommandLineRunner initDatabase(LaptopRepository repository) {

        return args -> {
            // for testing purposes
            String id1String = "160eeec5-1604-4c8c-b153-2cbdebca49dc";
            UUID id1 = UUID.fromString(id1String);
            Laptop laptop1 = new Laptop(id1, "HP", BigDecimal.valueOf(999.99),55.4,  new Location(52.521992, 13.413244));
            productService.saveLaptopIntoDB(laptop1);



            //log.info("Preloading " + repository.save(new LaptopLocationOnly(id1,new Location(52.521992, 13.413244))));

            //log.info("Preloading " + repository.save(new Laptop("HP", 999.99,55.4, new Location(52.521992, 13.413244))));
            //log.info("Preloading " + repository.save(new Laptop("Lenovo", 1299.99,65.6, new Location(22.222, 50.848))));
            //log.info("Preloading " + repository.save(new Laptop("MacBook", 2499.99,23.4, new Location(22.222, 50.848))));
        };
    }

}
