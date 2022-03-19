package com.futureSheep.ApplicationMS_kbe.controller;

import com.futureSheep.ApplicationMS_kbe.productService.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1")
public class GoogleController {

    @Autowired
    private ProductService productService;

    @GetMapping("/getLocationLaptop/{id}")
    String getLocation(Model model, @PathVariable UUID id) {
        model.addAttribute("lat", productService.getSingleLaptop(id).getContent().getLocation().getLat());
        model.addAttribute("lng", productService.getSingleLaptop(id).getContent().getLocation().getLng());

        return "laptop";
    }
/*
    @GetMapping("/laptops/{id}")
    String getLocation(Model model) {
        //model.addAttribute("location", "location");
        return "laptop";
    }*/
}
