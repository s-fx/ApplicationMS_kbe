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
        log.info("[DatawarehouseService] External Api at " + DATAWAREHOUSE_API + " is called with id: " + id);
        String url = DATAWAREHOUSE_API + "/{id}";
        Laptop laptop = restTemplate.getForObject(url, Laptop.class, id);
        return assembler.toModel(laptop);
    }

    public List<EntityModel<Laptop>> collectAllLaptopsFromDatawareHouse() {
        ResponseEntity<Laptop[]> response = restTemplate.getForEntity(DATAWAREHOUSE_API, Laptop[].class);
        Laptop[] laptopArray = response.getBody();
        log.info("[DatawarehouseService] Received Laptop Array Length : " + laptopArray.length);
        List<EntityModel<Laptop>> entityModelList = Arrays.stream(laptopArray).map(assembler::toModel).collect(Collectors.toList());
        System.out.println("EntityModelList: " + entityModelList);
        log.info("[DatawarehouseService] Collect all Laptops from Datawarehouse: " + laptopArray);
        return entityModelList;
    }

    public Laptop saveLaptopIntoDatawareHouseDB(Laptop laptop) {
        restTemplate.postForObject(DATAWAREHOUSE_API, laptop, Laptop.class);
        log.info("[DatawarehouseService] Save Laptop into Datawarehouse: " + laptop);
        return laptop;
    }

    public void deleteLaptopInDatawareHouseDB(UUID id) {
        String url = DATAWAREHOUSE_API + "/" + id.toString();
        restTemplate.delete(url);
        log.info("[DatawarehouseService] Delete Laptop from Datawarehouse with id: " + id);
    }

}
