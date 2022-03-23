package com.futureSheep.ApplicationMS_kbe.controller;

import com.futureSheep.ApplicationMS_kbe.productService.ProductService;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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



    /**
     * CollectionModel = HATEOAS container, encapsulate collection of laptop resources
     * why all these links? => makes it possible to evolve REST services over time
     * curl localhost:8080/api/laptops
     */
    @Operation(summary = "Get all laptops", description = "Get a list of all laptops in the datastorage", tags ="Laptop")
    @GetMapping()
    public CollectionModel<EntityModel<Laptop>> getAllLaptops() {
        List<EntityModel<Laptop>> laptops = productService.collectAllLaptops();
        log.info("GET Request at /laptops : " + laptops);
        return CollectionModel.of(laptops, linkTo(methodOn(LaptopController.class).getAllLaptops()).withSelfRel());
    }

    /**
     * curl -v -X POST localhost:8080/api/laptops -H 'Content-Type:application/json' -d '{"brand": "JAJAJAJA", "price": "229.99", "weight": "12.1"}'
     * why wildcard <?> ???
     */
    @Operation(summary = "Add laptop", description = "Add a new laptop to the datastorage", tags ="Laptop")
    @PostMapping()
    void addLaptop(@RequestBody Laptop newLaptop) {
        EntityModel<Laptop> entityModel = productService.validateAndSaveLaptop(newLaptop);
        log.info("POST Request /laptops : " + newLaptop);
        //return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    /**
     * Get a single Laptop by id
     * EntityModel<T> = HATEOAS container, containing data and collection of links
     * curl command: curl -v localhost:8080/api/laptops/UUID | json_pp
     * Link: includes a URI and a relation (see assembler)
     */
    @Operation(summary = "Get laptop", description = "Get laptop with the corresponding id", tags ="Laptop")
    @GetMapping("/{id}")
    public EntityModel<Laptop> getLaptop(@PathVariable UUID id) {
        log.info("GET Request /laptops/{id} : " + id);
        return productService.getSingleLaptopFromDatawarehouseAPI(id);
    }



    /**
     * curl -v -X DELETE localhost:8080/api/laptops/UUID
     */
    @Operation(summary = "Remove laptop", description = "Remove laptop with corresponding id from datastorage", tags ="Laptop")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteLaptop(@PathVariable UUID id) {
        productService.deleteLaptop(id);
        log.info("DELETE Request /laptops/{id} : " + id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Calculate MWS", description = "Calculate MWS for laptop with corresponding id", tags ="Laptop")
    @GetMapping("/calculateMWS/{id}")
    public double calculateMWSForLaptop(@PathVariable UUID id){
        double price = productService.getPriceOfLaptop(id);
        log.info("GET Request /laptops/calculateMWS/{id} : " + id);
        return productService.getMWSOfLaptop(price);
    }

}

