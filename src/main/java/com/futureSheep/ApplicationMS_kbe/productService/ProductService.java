package com.futureSheep.ApplicationMS_kbe.productService;

import com.futureSheep.ApplicationMS_kbe.calculatorService.CalculatorService;
import com.futureSheep.ApplicationMS_kbe.controller.exceptions.LaptopNotFoundException;
import com.futureSheep.ApplicationMS_kbe.dataStorage.LaptopModelAssembler;
import com.futureSheep.ApplicationMS_kbe.dataStorage.LaptopRepository;
import com.futureSheep.ApplicationMS_kbe.datawarehouseService.DatawarehouseServiceImpl;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import com.futureSheep.ApplicationMS_kbe.validation.LaptopValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Diese Klasse bearbeitet alle Dinge die im LaptopController nötig sind,
 * somit ist das repository im LaptopController nicht bekannt und abgekapselt ist
 * ist ProductService in seinem Diagram...
 */
@CommonsLog
@Service
@AllArgsConstructor
public class ProductService {


    //@Autowired
    //private LaptopRepository repository;
    // mach mal nicht final und schau ob es klappt
    private final LaptopRepository repository;

    //@Autowired
    private LaptopModelAssembler assembler;
    //@Autowired
    private CalculatorService calculatorService;
    //@Autowired
    private LaptopValidationService laptopValidationService;

    private DatawarehouseServiceImpl datawarehouseService;




    public List<EntityModel<Laptop>> collectAllLaptops() {
        return datawarehouseService.collectAllLaptopsFromDatawareHouse();
    }

    public EntityModel<Laptop> validateAndSaveLaptop(Laptop laptop) {
        EntityModel<Laptop> entityModel = assembler.toModel(laptop);
        String res = laptopValidationService.addLaptop(laptop);
        log.info(res);
        return entityModel;
    }


    public void saveLaptopIntoDB(Laptop laptop) {
        log.info("Saving " + laptop + " into DB");
        repository.save(laptop);
        datawarehouseService.saveLaptopIntoDatawareHouseDB(laptop);
    }




    public EntityModel<Laptop> getSingleLaptop(UUID id) {
        Laptop laptop = repository.findById(id).orElseThrow(() -> new LaptopNotFoundException(id));
        double mehrwertsteuer = getMWSOfLaptop(laptop.getPrice());
        laptop.setMehrwertsteuer(mehrwertsteuer);
        return assembler.toModel(laptop);
    }


    public void deleteLaptop(UUID id) {
        //if(repository.findById(id).isEmpty()) {
        //    throw new LaptopNotFoundException(id);
        //}
        //repository.deleteById(id);
        // was machen wir hier wenn notfoundByID????
        datawarehouseService.deleteLaptopInDatawareHouseDB(id);
        log.info("Laptop with id " + id + " deleted");
    }


    public double getPriceOfLaptop(UUID id) {
        Laptop laptop = repository.findById(id).orElseThrow(() -> new LaptopNotFoundException(id));
        log.info("Get price of laptop with id " + id);
        return laptop.getPrice();
    }


    public double getMWSOfLaptop(double price) {
        return calculatorService.getMWSOfLaptopFromExternalAPI(price);
    }


    /**
     * So jetzt holst du dir nicht mehr alle Laptops aus dieser DB sondern aus dem Datawarehouse
     * Location ist nur in der ApplicationDB
     *
     */



    public EntityModel<Laptop> getSingleLaptopFromDatawarehouseAPI(UUID id) {
        return datawarehouseService.getSingleLaptopFromDatawareHouse(id);
    }

}
