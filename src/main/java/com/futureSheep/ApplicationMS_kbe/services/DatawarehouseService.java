package com.futureSheep.ApplicationMS_kbe.services;

import com.futureSheep.ApplicationMS_kbe.configurations.LaptopModelAssembler;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CommonsLog
@AllArgsConstructor
@Service
public class DatawarehouseService {

    private LaptopModelAssembler assembler;

    @Autowired
    RestTemplate restTemplate;

    private final String DATAWAREHOUSE_API = "http://localhost:8080/api/v1/laptops";


    public EntityModel<Laptop> getSingleLaptopFromDatawareHouse(UUID id) {
        log.info("External Api at " + DATAWAREHOUSE_API + " is called with price value: " + id);
        String url = DATAWAREHOUSE_API + "/{id}";
        Laptop laptop = restTemplate.getForObject(url, Laptop.class, id);
        return assembler.toModel(laptop);
    }

    public List<EntityModel<Laptop>> collectAllLaptopsFromDatawareHouse() {
        ResponseEntity<Laptop[]> response = restTemplate.getForEntity(DATAWAREHOUSE_API, Laptop[].class);
        Laptop[] laptopArray = response.getBody();
        List<EntityModel<Laptop>> laptops = Arrays.stream(laptopArray).map(assembler::toModel).collect(Collectors.toList());
        return laptops;
    }

    public Laptop saveLaptopIntoDatawareHouseDB(Laptop laptop) {
        restTemplate.postForObject(DATAWAREHOUSE_API, laptop, Laptop.class);
        return laptop;
    }

    public void deleteLaptopInDatawareHouseDB(UUID id) {
        String url = DATAWAREHOUSE_API + "/" + id.toString();
        restTemplate.delete(url);
    }

}
