package com.futureSheep.ApplicationMS_kbe.productService;

import com.futureSheep.ApplicationMS_kbe.calculatorService.CalculatorService;
import com.futureSheep.ApplicationMS_kbe.controller.exceptions.LaptopNotFoundException;
import com.futureSheep.ApplicationMS_kbe.dataStorage.LaptopModelAssembler;
import com.futureSheep.ApplicationMS_kbe.dataStorage.LaptopRepository;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import com.futureSheep.ApplicationMS_kbe.validation.LaptopValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

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
public class ProductServiceImpl implements ProductService {


    //@Autowired
    //private LaptopRepository repository;
    private final LaptopRepository repository;

    //@Autowired
    private LaptopModelAssembler assembler;
    //@Autowired
    private CalculatorService calculatorService;
    //@Autowired
    private LaptopValidationService laptopValidationService;



    @Override
    public List<EntityModel<Laptop>> collectAllLaptops() {
        List<EntityModel<Laptop>> laptops = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());
        log.info("All Laptops collected from repository");
        for (EntityModel<Laptop> laptop : laptops) {
            Laptop lap = laptop.getContent();
            lap.setMehrwertsteuer(getMWSOfLaptop(lap.getPrice()));
        }

        return laptops;
    }

    @Override
    public EntityModel<Laptop> validateLaptopBeforeSavingIntoDB(Laptop laptop) {
        EntityModel<Laptop> entityModel = assembler.toModel(laptop);
        String res = laptopValidationService.addLaptop(laptop);
        log.info(res);
        return entityModel;
    }

    @Override
    public void saveLaptopIntoDB(Laptop laptop) {
        log.info("Saving " + laptop + " into DB");
        repository.save(laptop);
    }


    @Override
    public EntityModel<Laptop> getSingleLaptop(UUID id) {
        Laptop laptop = repository.findById(id).orElseThrow(() -> new LaptopNotFoundException(id));
        double mehrwertsteuer = getMWSOfLaptop(laptop.getPrice());
        laptop.setMehrwertsteuer(mehrwertsteuer);
        return assembler.toModel(laptop);
    }

    @Override
    public void deleteLaptop(UUID id) {
        if(repository.findById(id).isEmpty()) {
            throw new LaptopNotFoundException(id);
        }
        repository.deleteById(id);
        log.info("Laptop with id " + id + " deleted");
    }

    @Override
    public double getPriceOfLaptop(UUID id) {
        Laptop laptop = repository.findById(id).orElseThrow(() -> new LaptopNotFoundException(id));
        log.info("Get price of laptop with id " + id);
        return laptop.getPrice();
    }

    @Override
    public double getMWSOfLaptop(double price) {
        return calculatorService.getMWSOfLaptopFromExternalAPI(price);
    }


}
