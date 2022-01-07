package com.futureSheep.ApplicationMS_kbe.productService;

import com.futureSheep.ApplicationMS_kbe.calculatorService.CalculatorService;
import com.futureSheep.ApplicationMS_kbe.controller.exceptions.LaptopNotFoundException;
import com.futureSheep.ApplicationMS_kbe.dataStorage.LaptopModelAssembler;
import com.futureSheep.ApplicationMS_kbe.dataStorage.LaptopRepository;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import com.futureSheep.ApplicationMS_kbe.validation.LaptopValidationService;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    private LaptopRepository repository;
    @Autowired
    private LaptopModelAssembler assembler;
    @Autowired
    private CalculatorService calculatorService;
    @Autowired
    private LaptopValidationService laptopValidationService;

    @Override
    public List<EntityModel<Laptop>> collectAllLaptops() {
        List<EntityModel<Laptop>> laptops = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());
        System.out.println(laptops);
        return laptops;
    }

    @Override
    public EntityModel<Laptop> validateLaptopBeforeSavingIntoDB(Laptop laptop) {
        EntityModel<Laptop> entityModel = assembler.toModel(laptop);
        laptopValidationService.addLaptop(laptop);
        return entityModel;
    }

    @Override
    public void saveLaptopIntoDB(Laptop laptop) {
        repository.save(laptop);
    }


    @Override
    public EntityModel<Laptop> getSingleLaptop(UUID id) {
        // muss auch direkt mws mitgeben danach
        Laptop laptop = repository.findById(id).orElseThrow(() -> new LaptopNotFoundException(id));
        return assembler.toModel(laptop);
    }

    @Override
    public void deleteLaptop(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public double getPriceOfLaptop(UUID id) {
        Laptop laptop = repository.findById(id).orElseThrow(() -> new LaptopNotFoundException(id));
        return laptop.getPrice();
    }

    @Override
    public double getMWSOfLaptop(double price) {
        return calculatorService.getMWSOfLaptopFromExternalAPI(price);
    }


}
