package com.futureSheep.ApplicationMS_kbe.controller;

import com.futureSheep.ApplicationMS_kbe.controller.LaptopController;
import com.futureSheep.ApplicationMS_kbe.configurations.LaptopModelAssembler;
import com.futureSheep.ApplicationMS_kbe.repositories.LaptopRepository;
import com.futureSheep.ApplicationMS_kbe.services.ProductService;

import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import com.futureSheep.ApplicationMS_kbe.products.Location;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LaptopController.class)
public class LaptopControllerTest {

    @Autowired
    LaptopController laptopController;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @MockBean
    LaptopRepository repository;


    private Laptop laptop_1;
    private Laptop laptop_2;
    private EntityModel<Laptop> laptopEntityModel;
    List<EntityModel<Laptop>> allLaptops;


    @BeforeEach
    public void setUp() {
        LaptopModelAssembler assembler = new LaptopModelAssembler();
        laptop_1 = new Laptop(UUID.randomUUID(), "HP", BigDecimal.valueOf(999.99), 55.4, new Location(52.521992, 13.413244));
        laptop_2 = new Laptop(UUID.randomUUID(), "Dell", BigDecimal.valueOf(9.9), 45.4, new Location(50.521992, 13.413244));
        laptopEntityModel = assembler.toModel(laptop_1);
        List<Laptop> laptops = Arrays.asList(laptop_1, laptop_2);
        allLaptops = laptops.stream().map(assembler::toModel).collect(Collectors.toList());
    }


    @Test
    public void getAllLaptops_success() throws Exception {

        given(productService.collectAllLaptops()).willReturn(allLaptops);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/laptops")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..laptopList[0].brand").value("HP"))
                .andExpect(MockMvcResultMatchers.jsonPath("$..laptopList[1].brand").value("Dell"))
                .andExpect(MockMvcResultMatchers.jsonPath("$..laptopList[0].price").value(999.99));
    }

    @Test
    public void getSingleLaptop_success() throws Exception {
        UUID id = UUID.randomUUID();

        EntityModel<Laptop> entity_laptop = new EntityModel<>(laptop_1);
        given(productService.getSingleLaptop(id)).willReturn(entity_laptop);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/laptops/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..brand").value("HP"));
    }
}
