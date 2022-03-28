package com.futureSheep.ApplicationMS_kbe.services;

import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@CommonsLog
@Service("LaptopValidationService")
public class LaptopValidationService {

    private Validator validator;
    private ProductService productService;

    @Autowired
    public LaptopValidationService(Validator validator, @Lazy ProductService productService) {
        this.validator = validator;
        this.productService = productService;
    }


    public String addLaptop(Laptop laptop) {
        Set<ConstraintViolation<Laptop>> violations = validator.validate(laptop);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Laptop> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            log.warn("[LaptopValidationService] Error occurred validating Laptop " + sb + " " + violations);
            throw new ConstraintViolationException("Error occurred: " + sb, violations);
        }
        productService.saveLaptopIntoDB(laptop);
        return "Laptop: " + laptop.getBrand() + " was added";

    }
}
