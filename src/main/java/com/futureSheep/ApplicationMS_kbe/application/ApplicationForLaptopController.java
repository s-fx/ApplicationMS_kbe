package com.futureSheep.ApplicationMS_kbe.application;

import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.UUID;

public interface ApplicationForLaptopController {

    List<EntityModel<Laptop>> collectAllLaptops();

    EntityModel<Laptop> saveLaptopIntoDB(Laptop laptop);

    EntityModel<Laptop> getSingleLaptop(UUID id);

    boolean deleteLaptop(UUID id);

    double getPriceOfLaptop(UUID id);





}
