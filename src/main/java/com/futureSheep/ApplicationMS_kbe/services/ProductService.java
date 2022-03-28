package com.futureSheep.ApplicationMS_kbe.services;

import com.futureSheep.ApplicationMS_kbe.configurations.LaptopModelAssembler;
import com.futureSheep.ApplicationMS_kbe.products.LaptopLocationOnly;
import com.futureSheep.ApplicationMS_kbe.products.Location;
import com.futureSheep.ApplicationMS_kbe.repositories.LaptopRepository;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@CommonsLog
@Service
@AllArgsConstructor
public class ProductService {

    private final LaptopRepository repository;
    private LaptopModelAssembler assembler;
    private CalculatorService calculatorService;
    private LaptopValidationService laptopValidationService;
    private DatawarehouseService datawarehouseService;


    public List<EntityModel<Laptop>> collectAllLaptops() {
        List<EntityModel<Laptop>> entityModelList = datawarehouseService.collectAllLaptopsFromDatawareHouse();
        setLocationAndMWS(entityModelList);
        log.info("[ProductService] Collect all Laptops: " + entityModelList);
        return entityModelList;
    }


    public EntityModel<Laptop> validateAndSaveLaptop(Laptop laptop) {
        EntityModel<Laptop> entityModel = assembler.toModel(laptop);
        String res = laptopValidationService.addLaptop(laptop);
        log.info("[ProductService] Validation Service: " + res);
        return entityModel;
    }


    public void saveLaptopIntoDB(Laptop laptop) {
        UUID id = laptop.getId();
        Location location = laptop.getLocation();
        LaptopLocationOnly laptopLocationOnly = new LaptopLocationOnly(id, location);
        repository.save(laptopLocationOnly);
        datawarehouseService.saveLaptopIntoDatawareHouseDB(laptop);
        log.info("[ProductService] save Laptop into DB " + laptop);
    }


    public EntityModel<Laptop> getSingleLaptop(UUID id) {
        EntityModel<Laptop> laptop = datawarehouseService.getSingleLaptopFromDatawareHouse(id);
        BigDecimal mehrwertsteuer = getMWSOfLaptop(laptop.getContent().getPrice());
        laptop.getContent().setMehrwertsteuer(mehrwertsteuer);
        laptop.getContent().setLocation(repository.findById(id).get().getLocation());
        log.info("[ProductService] Get single Laptop with id: " + id);
        return laptop;
    }


    public void deleteLaptop(UUID id) {
        datawarehouseService.deleteLaptopInDatawareHouseDB(id);
        log.info("[ProductService] Delete Laptop with id: " + id);
    }


    public BigDecimal getPriceOfLaptop(UUID id) {
        EntityModel<Laptop> laptopEntityModel = datawarehouseService.getSingleLaptopFromDatawareHouse(id);
        log.info("[ProductService] Get price of laptop with id " + id);
        return laptopEntityModel.getContent().getPrice();
    }

    public BigDecimal getMWSOfLaptop(BigDecimal price) {
        log.info("[ProductService] get MWS of Laptop with price: " + price);
        return calculatorService.getMWSOfLaptopFromExternalAPI(price);
    }


    private List<EntityModel<Laptop>> setLocationAndMWS(List<EntityModel<Laptop>> entityModelList) {
        for (EntityModel<Laptop> entityModel : entityModelList) {
            Laptop laptop = entityModel.getContent();
            UUID id = laptop.getId();
            Location location = repository.findById(id).get().getLocation();
            laptop.setLocation(location);
            BigDecimal mehrwertsteuer = getMWSOfLaptop(laptop.getPrice());
            laptop.setMehrwertsteuer(mehrwertsteuer);
        }
        return entityModelList;
    }

}
