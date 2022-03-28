package com.futureSheep.ApplicationMS_kbe.controller;

import com.futureSheep.ApplicationMS_kbe.services.ProductService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@CommonsLog
@Controller
@RequestMapping("/api/v1/laptops")
public class GoogleController {

    private final ProductService productService;

    public GoogleController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getLocation/{id}")
    String getLocation(Model model, @PathVariable UUID id) {
        model.addAttribute("lat", productService.getSingleLaptop(id).getContent().getLocation().getLat());
        model.addAttribute("lng", productService.getSingleLaptop(id).getContent().getLocation().getLng());
        log.info("[GoogleController] Get Location of Laptop with ID: " + id);
        return "laptop";
    }

}
