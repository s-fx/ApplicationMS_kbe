package com.futureSheep.ApplicationMS_kbe.controller;

import com.futureSheep.ApplicationMS_kbe.controller.exceptions.LaptopNotFoundException;
import com.futureSheep.ApplicationMS_kbe.dataStorage.LaptopModelAssembler;
import com.futureSheep.ApplicationMS_kbe.dataStorage.LaptopRepository;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import com.futureSheep.ApplicationMS_kbe.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class LaptopController {

    private final LaptopRepository repository;
    private final LaptopModelAssembler assembler;

    @Autowired
    private CalculatorService calculatorService;


    public LaptopController(LaptopRepository repository, LaptopModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    /**
     * CollectionModel = HATEOAS container, encapsulate collection of laptop resources
     * why all these links? => makes it possible to evolve REST services over time
     * curl localhost:8080/api/laptops
     * @return CollectionModel of laptops
     */
    @GetMapping("/laptops")
    public CollectionModel<EntityModel<Laptop>> getAllLaptops() {
        List<EntityModel<Laptop>> laptops = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(laptops, linkTo(methodOn(LaptopController.class).getAllLaptops()).withSelfRel());
    }

    /**
     * curl -v -X POST localhost:8080/api/laptops -H 'Content-Type:application/json' -d '{"brand": "JAJAJAJA", "price": "229.99", "weight": "12.1"}'
     * warum wildcard <?> ???
     * @param newLaptop stored in db
     * @return 201 HTTP Created response
     */
    @PostMapping("/laptops")
    ResponseEntity<?> addLaptop(@RequestBody Laptop newLaptop) {
        EntityModel<Laptop> entityModel = assembler.toModel(newLaptop);
        /**
         * hier nicht merh saven sonder validator.addLaptop(laptop)
         * und im validaotr wird gesafet
         */
        repository.save(newLaptop);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    /**
     * Get a single Laptop by id
     * EntityModel<T> = HATEOAS container, containing data and collection of links
     * curl command: curl -v localhost:8080/api/laptops/UUID | json_pp
     * Link: includes a URI and a relation (see assembler)
     * @param id of laptop
     * @return model of laptop through assembler
     */
    @GetMapping("/laptops/{id}")
    public EntityModel<Laptop> getLaptop(@PathVariable UUID id) {
        Laptop laptop = repository.findById(id).orElseThrow(() -> new LaptopNotFoundException(id));
        return assembler.toModel(laptop);
    }

    /**
     * curl -v -X PUT localhost:8080/api/laptops/UUID -H 'Content-Type:application/json' -d '{"brand": "UPDATED_LAPTOP", "price": "499.99", "weight": "99.99"}'
     * @param newLaptop laptop
     * @param id of laptop
     * @return save into h2
     */
    @PutMapping("/laptops/{id}")
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
    }

    /**
     * curl -v -X DELETE localhost:8080/api/laptops/UUID
     * @param id of laptop
     * @return responseEntity
     */
    @DeleteMapping("/laptops/{id}")
    ResponseEntity<?> deleteLaptop(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/laptops/calculateMWS/{id}")
    public double calculateMWSForLaptop(@PathVariable UUID id){
        Laptop laptop = repository.findById(id).orElseThrow(() -> new LaptopNotFoundException(id));
        double price = laptop.getPrice();
        return calculatorService.getMWSOfLaptopFromExternalAPI(price);
    }


}

