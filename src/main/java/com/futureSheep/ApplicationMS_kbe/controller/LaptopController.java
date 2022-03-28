package com.futureSheep.ApplicationMS_kbe.controller;

import com.futureSheep.ApplicationMS_kbe.services.ProductService;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CommonsLog
@RestController
@RequestMapping("/api/v1/laptops")
public class LaptopController {

    private final ProductService productService;

    public LaptopController(ProductService productService) {
        this.productService = productService;
    }


    @Operation(summary = "Get all laptops", description = "Get a list of all laptops in the datastorage", tags = "Laptop")
    @GetMapping
    public CollectionModel<EntityModel<Laptop>> getAllLaptops() {
        List<EntityModel<Laptop>> laptops = productService.collectAllLaptops();
        log.info("[LaptopController] GET Request at /laptops : " + laptops);
        return CollectionModel.of(laptops, linkTo(methodOn(LaptopController.class).getAllLaptops()).withSelfRel());
    }


    @Operation(summary = "Add laptop", description = "Add a new laptop to the datastorage", tags = "Laptop")
    @PostMapping
    void addLaptop(@RequestBody Laptop newLaptop) {
        productService.validateAndSaveLaptop(newLaptop);
        log.info("[LaptopController] POST Request /laptops : " + newLaptop);
    }


    @Operation(summary = "Get laptop", description = "Get laptop with the corresponding id", tags = "Laptop")
    @GetMapping("/{id}")
    public EntityModel<Laptop> getLaptop(@PathVariable UUID id) {
        log.info("[LaptopController] GET Request /laptops/{id} : " + id);
        return productService.getSingleLaptop(id);
    }


    @Operation(summary = "Remove laptop", description = "Remove laptop with corresponding id from datastorage", tags = "Laptop")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteLaptop(@PathVariable UUID id) {
        productService.deleteLaptop(id);
        log.info("[LaptopController] DELETE Request /laptops/{id} : " + id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Calculate MWS", description = "Calculate MWS for laptop with corresponding id", tags = "Laptop")
    @GetMapping("/calculateMWS/{id}")
    public BigDecimal calculateMWSForLaptop(@PathVariable UUID id) {
        BigDecimal price = productService.getPriceOfLaptop(id);
        log.info("[LaptopController] GET Request /laptops/calculateMWS/{id} : " + id);
        return productService.getMWSOfLaptop(price);
    }

}

