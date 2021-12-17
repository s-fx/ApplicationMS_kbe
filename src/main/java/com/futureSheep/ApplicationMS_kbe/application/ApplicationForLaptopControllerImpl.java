package com.futureSheep.ApplicationMS_kbe.application;

import com.futureSheep.ApplicationMS_kbe.controller.exceptions.LaptopNotFoundException;
import com.futureSheep.ApplicationMS_kbe.dataStorage.LaptopModelAssembler;
import com.futureSheep.ApplicationMS_kbe.dataStorage.LaptopRepository;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Diese Klasse bearbeitet alle Dinge die im LaptopController nötig sind,
 * somit ist das repository im LaptopController nicht bekannt und abgekapselt ist
 */
@Service
public class ApplicationForLaptopControllerImpl implements ApplicationForLaptopController {


    @Autowired
    private LaptopRepository repository;
    @Autowired
    private LaptopModelAssembler assembler;

    @Override
    public List<EntityModel<Laptop>> collectAllLaptops() {
        List<EntityModel<Laptop>> laptops = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());
        System.out.println(laptops);
        return laptops;
    }

    @Override
    public EntityModel<Laptop> saveLaptopIntoDB(Laptop laptop) {
        EntityModel<Laptop> entityModel = assembler.toModel(laptop);
        /**
         * hier nicht mehr saven sondern validator.save(laptop)
         * und im validator wird gesafet
         */
        repository.save(laptop);
        return entityModel;
    }

    @Override
    public EntityModel<Laptop> getSingleLaptop(UUID id) {
        Laptop laptop = repository.findById(id).orElseThrow(() -> new LaptopNotFoundException(id));
        return assembler.toModel(laptop);
    }

    @Override
    public boolean deleteLaptop(UUID id) {
        repository.deleteById(id);
        return false;
    }

    @Override
    public double getPriceOfLaptop(UUID id) {
        Laptop laptop = repository.findById(id).orElseThrow(() -> new LaptopNotFoundException(id));
        return laptop.getPrice();
    }
}
