package com.futureSheep.ApplicationMS_kbe.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.futureSheep.ApplicationMS_kbe.services.ProductService;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {LaptopController.class})
@ExtendWith(SpringExtension.class)
class LaptopControllerTest {
    @Autowired
    private LaptopController laptopController;

    @MockBean
    private ProductService productService;


    @Test
    void testDeleteLaptop() throws Exception {
        doNothing().when(this.productService).deleteLaptop((UUID) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/laptops/{id}",
                UUID.randomUUID());
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.laptopController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testDeleteLaptop2() throws Exception {
        doNothing().when(this.productService).deleteLaptop((UUID) any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/api/v1/laptops/{id}",
                UUID.randomUUID());
        deleteResult.contentType("https://example.org/example");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.laptopController)
                .build()
                .perform(deleteResult);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testCalculateMWSForLaptop() throws Exception {
        //when(this.productService.getMWSOfLaptop(anyDouble())).thenReturn(10.0d);
        when(this.productService.getPriceOfLaptop((UUID) any())).thenReturn(BigDecimal.valueOf(9.9));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/laptops/calculateMWS/{id}",
                UUID.randomUUID());
        MockMvcBuilders.standaloneSetup(this.laptopController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("10.0"));
    }

    @Test
    void testCalculateMWSForLaptop2() throws Exception {
        //when(this.productService.getMWSOfLaptop(anyDouble())).thenReturn(10.0d);
        when(this.productService.getPriceOfLaptop((UUID) any())).thenReturn(BigDecimal.valueOf(9.9));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v1/laptops/calculateMWS/{id}",
                UUID.randomUUID());
        getResult.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(this.laptopController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("10.0"));
    }

    @Test
    void testGetAllLaptops() throws Exception {
        when(this.productService.collectAllLaptops()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/laptops");
        MockMvcBuilders.standaloneSetup(this.laptopController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/v1/laptops\"}],\"content\":[]}"));
    }

    @Test
    void testGetAllLaptops2() throws Exception {
        when(this.productService.collectAllLaptops()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v1/laptops");
        getResult.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(this.laptopController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/v1/laptops\"}],\"content\":[]}"));
    }

    @Test
    void testGetAllLaptops3() throws Exception {
        when(this.productService.collectAllLaptops()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v1/laptops");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(this.laptopController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/v1/laptops\"}],\"content\":[]}"));
    }

    @Test
    void testGetLaptop() throws Exception {
        Iterable<Link> iterable = (Iterable<Link>) mock(Iterable.class);
        doNothing().when(iterable).forEach((java.util.function.Consumer<? super Link>) any());
        EntityModel<Laptop> entityModel = new EntityModel<>(new Laptop(), iterable);

        when(this.productService.getSingleLaptop((UUID) any())).thenReturn(entityModel);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/laptops/{id}",
                UUID.randomUUID());
        MockMvcBuilders.standaloneSetup(this.laptopController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"brand\":null,\"price\":0.0,\"weight\":0.0,\"location\":null,\"mehrwertsteuer\":0.0,\"links\":[]}"));
    }

    @Test
    void testGetLaptop2() throws Exception {
        Iterable<Link> iterable = (Iterable<Link>) mock(Iterable.class);
        doNothing().when(iterable).forEach((java.util.function.Consumer<? super Link>) any());
        EntityModel<Laptop> entityModel = new EntityModel<>(new Laptop(), iterable);

        when(this.productService.getSingleLaptop((UUID) any())).thenReturn(entityModel);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v1/laptops/{id}", UUID.randomUUID());
        getResult.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(this.laptopController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"brand\":null,\"price\":0.0,\"weight\":0.0,\"location\":null,\"mehrwertsteuer\":0.0,\"links\":[]}"));
    }
}

