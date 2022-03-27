package com.futureSheep.ApplicationMS_kbe.configurations;

import com.futureSheep.ApplicationMS_kbe.controller.LaptopController;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component("LaptopModelAssembler")
public class LaptopModelAssembler implements RepresentationModelAssembler<Laptop, EntityModel<Laptop>> {


    @Override
    public EntityModel<Laptop> toModel(Laptop laptop) {
        return EntityModel.of(laptop, //
                linkTo(methodOn(LaptopController.class).getLaptop(laptop.getId())).withSelfRel(),
                linkTo(methodOn(LaptopController.class).getAllLaptops()).withRel("laptops"));
    }
}