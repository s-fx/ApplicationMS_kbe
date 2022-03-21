package com.futureSheep.ApplicationMS_kbe.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.futureSheep.ApplicationMS_kbe.productService.ProductService;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import com.futureSheep.ApplicationMS_kbe.products.Location;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {GoogleController.class})
@ExtendWith(SpringExtension.class)
class GoogleControllerTest {
    @Autowired
    private GoogleController googleController;

    @MockBean
    private ProductService productService;

    @Test
    void testGetLocation() throws Exception {
        Location location = new Location();
        location.setLat(10.0d);
        location.setLng(10.0d);

        Laptop laptop = new Laptop();
        laptop.setLocation(location);
        Iterable<Link> iterable = (Iterable<Link>) mock(Iterable.class);
        doNothing().when(iterable).forEach((java.util.function.Consumer<? super Link>) any());
        EntityModel<Laptop> entityModel = new EntityModel<>(laptop, iterable);

        when(this.productService.getSingleLaptop((UUID) any())).thenReturn(entityModel);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/getLocationLaptop/{id}",
                UUID.randomUUID());
        MockMvcBuilders.standaloneSetup(this.googleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lat", "lng"))
                .andExpect(MockMvcResultMatchers.view().name("laptop"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("laptop"));
    }
}

