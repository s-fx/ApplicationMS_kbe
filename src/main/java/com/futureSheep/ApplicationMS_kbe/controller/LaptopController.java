package com.futureSheep.ApplicationMS_kbe.controller;

import com.futureSheep.ApplicationMS_kbe.application.ApplicationForLaptopController;
import com.futureSheep.ApplicationMS_kbe.application.ApplicationForLaptopControllerImpl;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import com.futureSheep.ApplicationMS_kbe.calculatorService.CalculatorService;
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

@RestController
@RequestMapping("/api")
public class LaptopController {

    @Autowired
    private CalculatorService calculatorService;

    @Autowired
    private ApplicationForLaptopController application = new ApplicationForLaptopControllerImpl();


    /**
     * CollectionModel = HATEOAS container, encapsulate collection of laptop resources
     * why all these links? => makes it possible to evolve REST services over time
     * curl localhost:8080/api/laptops
     */
    @GetMapping("/laptops")
    public CollectionModel<EntityModel<Laptop>> getAllLaptops() {
        List<EntityModel<Laptop>> laptops = application.collectAllLaptops();
        System.out.println(laptops);
        return CollectionModel.of(laptops, linkTo(methodOn(LaptopController.class).getAllLaptops()).withSelfRel());
    }

    /**
     * curl -v -X POST localhost:8080/api/laptops -H 'Content-Type:application/json' -d '{"brand": "JAJAJAJA", "price": "229.99", "weight": "12.1"}'
     * why wildcard <?> ???
     */
    @PostMapping("/laptops")
    ResponseEntity<?> addLaptop(@RequestBody Laptop newLaptop) {
        EntityModel<Laptop> entityModel = application.saveLaptopIntoDB(newLaptop);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    /**
     * Get a single Laptop by id
     * EntityModel<T> = HATEOAS container, containing data and collection of links
     * curl command: curl -v localhost:8080/api/laptops/UUID | json_pp
     * Link: includes a URI and a relation (see assembler)
     */
    @GetMapping("/laptops/{id}")
    public EntityModel<Laptop> getLaptop(@PathVariable UUID id) {
        return application.getSingleLaptop(id);
    }


    /**
     * curl -v -X DELETE localhost:8080/api/laptops/UUID
     */
    @DeleteMapping("/laptops/{id}")
    ResponseEntity<?> deleteLaptop(@PathVariable UUID id) {
        application.deleteLaptop(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/laptops/calculateMWS/{id}")
    public double calculateMWSForLaptop(@PathVariable UUID id){
        double price = application.getPriceOfLaptop(id);
        return calculatorService.getMWSOfLaptopFromExternalAPI(price);
    }


    /*    *
     * curl -v -X PUT localhost:8080/api/laptops/UUID -H 'Content-Type:application/json' -d '{"brand": "UPDATED_LAPTOP", "price": "499.99", "weight": "99.99"}'
     * @param newLaptop laptop
     * @param id of laptop
     * @return save into h2*/
/*    @PutMapping("/laptops/{id}")
    ResponseEntity<?> replaceLaptop(@RequestBody Laptop newLaptop, @PathVariable UUID id) {
        Laptop updatedLaptop = repository.findById(id) //
                .map(laptop -> {
                    laptop.setBrand(newLaptop.getBrand());
                    laptop.setPrice(newLaptop.getPrice());
                    return repository.save(laptop);
                }) //
                .orElseGet(() -> {
                    newLaptop.setId(id);
                    return repository.save(newLaptop);
                });
        EntityModel<Laptop> entityModel = assembler.toModel(updatedLaptop);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }*/


}

