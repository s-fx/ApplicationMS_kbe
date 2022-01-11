package com.futureSheep.ApplicationMS_kbe.dataStorage;

import com.futureSheep.ApplicationMS_kbe.products.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;


@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(LaptopRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Laptop("HP", 999.99,55.4, new Location(52.521992, 13.413244))));
            log.info("Preloading " + repository.save(new Laptop("Lenovo", 1299.99,65.6, new Location(22.222, 50.848))));
            log.info("Preloading " + repository.save(new Laptop("MacBook", 2499.99,23.4, new Location(22.222, 50.848))));
/*            log.info("Preloading " + repository.save(new Laptop("Dell XPS", 799.99,65.7)));
            log.info("Preloading " + repository.save(new Laptop("ThinkPad T490", 1099.99,44.7)));
            log.info("Preloading " + repository.save(new Laptop("IdeaPad", 699.99,98.9)));
            log.info("Preloading " + repository.save(new Laptop("Alienware", 1899.99,9.9)));
            log.info("Preloading " + repository.save(new Laptop("Dell N1", 299.99,98.0)));
            log.info("Preloading " + repository.save(new Laptop("ThinkPad T590", 2299.99,11.1)));
            log.info("Preloading " + repository.save(new Laptop("Lenovo X1", 2999.99,21.2)));*/
        };
    }

}
