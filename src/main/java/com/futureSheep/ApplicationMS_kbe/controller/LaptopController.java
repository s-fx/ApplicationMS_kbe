package com.futureSheep.ApplicationMS_kbe.controller;

import com.futureSheep.ApplicationMS_kbe.productService.ProductService;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CommonsLog
@RestController
@RequestMapping("/api")
public class LaptopController {

    @Autowired
    private ProductService productService;


    @GetMapping("/laptops")
    public CollectionModel<EntityModel<Laptop>> getAllLaptops() {
        List<EntityModel<Laptop>> laptops = productService.collectAllLaptops();
        log.info("GET Request at /laptops : " + laptops);
        return CollectionModel.of(laptops, linkTo(methodOn(LaptopController.class).getAllLaptops()).withSelfRel());
    }


    @PostMapping("/laptops")
    ResponseEntity<?> addLaptop(@RequestBody Laptop newLaptop) {
        EntityModel<Laptop> entityModel = productService.validateLaptopBeforeSavingIntoDB(newLaptop);
        log.info("POST Request /laptops : " + newLaptop);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }


    @GetMapping("/laptops/{id}")
    public EntityModel<Laptop> getLaptop(@PathVariable UUID id) {
        log.info("GET Request /laptops/{id} : " + id);
        return productService.getSingleLaptop(id);
    }


    @DeleteMapping("/laptops/{id}")
    ResponseEntity<?> deleteLaptop(@PathVariable UUID id) {
        productService.deleteLaptop(id);
        log.info("DELETE Request /laptops/{id} : " + id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/laptops/calculateMWS/{id}")
    public double calculateMWSForLaptop(@PathVariable UUID id){
        double price = productService.getPriceOfLaptop(id);
        log.info("GET Request /laptops/calculateMWS/{id} : " + id);
        return productService.getMWSOfLaptop(price);
    }

}

