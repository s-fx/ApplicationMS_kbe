package com.futureSheep.ApplicationMS_kbe;

import com.futureSheep.ApplicationMS_kbe.controller.LaptopController;
import com.futureSheep.ApplicationMS_kbe.dataStorage.LaptopModelAssembler;
import com.futureSheep.ApplicationMS_kbe.dataStorage.LaptopRepository;
import com.futureSheep.ApplicationMS_kbe.productService.ProductService;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import com.futureSheep.ApplicationMS_kbe.products.Location;

import com.google.gson.Gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LaptopController.class)
public class LaptopControllerTest {

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
    public void setUp(){
        LaptopModelAssembler assembler = new LaptopModelAssembler();
        laptop_1 = new Laptop("HP", 999.99,55.4, new Location(52.521992, 13.413244));
        laptop_2 = new Laptop("Dell", 899.99,45.4, new Location(50.521992, 13.413244));
        laptopEntityModel = assembler.toModel(laptop_1);
        List<Laptop> laptops = Arrays.asList(laptop_1,laptop_2);
        allLaptops = laptops.stream().map(assembler::toModel).collect(Collectors.toList());
    }



    @Test
    public void getAllLaptops_success() throws Exception {

        given(productService.collectAllLaptops()).willReturn(allLaptops);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/laptops")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..laptopList[0].brand").value("HP"))
                .andExpect(MockMvcResultMatchers.jsonPath("$..laptopList[1].brand").value("Dell"))
                .andExpect(MockMvcResultMatchers.jsonPath("$..laptopList[0].price").value(999.99));
    }

    @Test
    public void createLaptop_success() throws Exception {
        UUID id = UUID.randomUUID();
        laptop_1.setId(id);

        Mockito.when(repository.save(laptop_1)).thenReturn(laptop_1);
        Mockito.when(productService.validateLaptopBeforeSavingIntoDB(laptop_1)).thenReturn(laptopEntityModel);


        Gson gson = new Gson();
        String json = gson.toJson(laptop_1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/laptops").
                        contentType(MediaType.APPLICATION_JSON).
                        content(json)).
                andExpect(status().isCreated());
        verify(productService,times(1)).validateLaptopBeforeSavingIntoDB(any());
    }
}
