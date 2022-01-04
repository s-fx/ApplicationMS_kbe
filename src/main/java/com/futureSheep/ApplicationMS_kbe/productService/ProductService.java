package com.futureSheep.ApplicationMS_kbe.productService;

import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<EntityModel<Laptop>> collectAllLaptops();

    EntityModel<Laptop> saveLaptopIntoDB(Laptop laptop);

    EntityModel<Laptop> getSingleLaptop(UUID id);

    boolean deleteLaptop(UUID id);

    double getPriceOfLaptop(UUID id);

    double getMWSOfLaptop(double price);

    void saveLaptopIntoDB2(Laptop laptop);





}
