package com.futureSheep.ApplicationMS_kbe.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.futureSheep.ApplicationMS_kbe.configurations.LaptopModelAssembler;
import com.futureSheep.ApplicationMS_kbe.exceptions.LaptopNotFoundException;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import com.futureSheep.ApplicationMS_kbe.products.LaptopLocationOnly;
import com.futureSheep.ApplicationMS_kbe.products.Location;
import com.futureSheep.ApplicationMS_kbe.repositories.LaptopRepository;

import java.math.BigDecimal;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ProductService.class})
@ExtendWith(SpringExtension.class)
class ProductServiceTest {
    @MockBean(name = "CalculatorService")
    private CalculatorService calculatorService;

    @MockBean
    private DatawarehouseService datawarehouseService;

    @MockBean(name = "LaptopModelAssembler")
    private LaptopModelAssembler laptopModelAssembler;

    @MockBean
    private LaptopRepository laptopRepository;

    @MockBean(name = "LaptopValidationService")
    private LaptopValidationService laptopValidationService;

    @Autowired
    private ProductService productService;

    private Laptop laptop_1;
    private Laptop laptop_2;
    private Location location;
    private UUID id_1 = UUID.randomUUID();
    private UUID id_2 = UUID.randomUUID();
    private EntityModel<Laptop> laptopEntityModel;
    List<EntityModel<Laptop>> allLaptops;


    @BeforeEach
    public void setUp() {
        LaptopModelAssembler assembler = new LaptopModelAssembler();
        location = new Location(52.521992, 13.413244);
        laptop_1 = new Laptop(id_1, "HP", BigDecimal.valueOf(999.99), 99.9, location);
        laptop_2 = new Laptop(id_2, "Dell", BigDecimal.valueOf(888.8), 88.8, location);
        laptopEntityModel = assembler.toModel(laptop_1);
        List<Laptop> laptops = Arrays.asList(laptop_1, laptop_2);
        allLaptops = laptops.stream().map(assembler::toModel).collect(Collectors.toList());
    }



    @Test
    void testCollectAllLaptops() {
        LaptopLocationOnly laptopLocationOnly = new LaptopLocationOnly();
        laptopLocationOnly.setId(UUID.randomUUID());
        laptopLocationOnly.setLocation(location);

        Optional<LaptopLocationOnly> ofResult = Optional.of(laptopLocationOnly);
        when(this.laptopRepository.findById((UUID) any())).thenReturn(ofResult);

        Iterable<Link> iterable = (Iterable<Link>) mock(Iterable.class);
        doNothing().when(iterable).forEach(any());
        EntityModel<Laptop> e = new EntityModel<>(new Laptop(), iterable);

        ArrayList<EntityModel<Laptop>> entityModelList = new ArrayList<>();
        entityModelList.add(e);

        when(this.datawarehouseService.collectAllLaptopsFromDatawareHouse()).thenReturn(entityModelList);
        when(this.calculatorService.getMWSOfLaptopFromExternalAPI((BigDecimal) any())).thenReturn(BigDecimal.valueOf(42L));

        List<EntityModel<Laptop>> actualCollectAllLaptopsResult = this.productService.collectAllLaptops();

        assertSame(entityModelList, actualCollectAllLaptopsResult);
        assertEquals(1, actualCollectAllLaptopsResult.size());
        assertEquals("42", actualCollectAllLaptopsResult.get(0).getContent().getMehrwertsteuer().toString());
        verify(this.laptopRepository).findById((UUID) any());
        verify(this.datawarehouseService).collectAllLaptopsFromDatawareHouse();
        verify(iterable).forEach(any());
        verify(this.calculatorService).getMWSOfLaptopFromExternalAPI((BigDecimal) any());
    }


    @Test
    void testValidateAndSaveLaptop() {
        when(this.laptopValidationService.addLaptop(laptop_1)).thenReturn("Add Laptop");
        Iterable<Link> iterable = (Iterable<Link>) mock(Iterable.class);
        doNothing().when(iterable).forEach(any());
        EntityModel<Laptop> entityModel = new EntityModel<>(laptop_1, iterable);

        when(this.laptopModelAssembler.toModel(laptop_1)).thenReturn(entityModel);
        assertSame(entityModel, this.productService.validateAndSaveLaptop(laptop_1));
        verify(this.laptopValidationService).addLaptop(laptop_1);
        verify(this.laptopModelAssembler).toModel(laptop_1);
        verify(iterable).forEach(any());
    }

    @Test
    void testSaveLaptopIntoDB() {
        LaptopLocationOnly laptopLocationOnly = new LaptopLocationOnly();
        laptopLocationOnly.setId(id_1);
        laptopLocationOnly.setLocation(location);
        when(this.laptopRepository.save(laptopLocationOnly)).thenReturn(laptopLocationOnly);
        when(this.datawarehouseService.saveLaptopIntoDatawareHouseDB(laptop_1)).thenReturn(laptop_1);
        this.productService.saveLaptopIntoDB(laptop_1);
        verify(this.datawarehouseService).saveLaptopIntoDatawareHouseDB(laptop_1);
    }


    @Test
    void testGetSingleLaptop() {
        LaptopLocationOnly laptopLocationOnly = new LaptopLocationOnly();
        laptopLocationOnly.setId(UUID.randomUUID());
        laptopLocationOnly.setLocation(location);

        Optional<LaptopLocationOnly> ofResult = Optional.of(laptopLocationOnly);
        when(this.laptopRepository.findById((UUID) any())).thenReturn(ofResult);

        Iterable<Link> iterable = (Iterable<Link>) mock(Iterable.class);
        doNothing().when(iterable).forEach(any());
        EntityModel<Laptop> entityModel = new EntityModel<>(new Laptop(), iterable);

        when(this.datawarehouseService.getSingleLaptopFromDatawareHouse((UUID) any())).thenReturn(entityModel);
        when(this.calculatorService.getMWSOfLaptopFromExternalAPI((BigDecimal) any())).thenReturn(BigDecimal.valueOf(42L));

        EntityModel<Laptop> actualSingleLaptop = this.productService.getSingleLaptop(UUID.randomUUID());

        assertSame(entityModel, actualSingleLaptop);
        assertEquals("42", actualSingleLaptop.getContent().getMehrwertsteuer().toString());
        verify(this.laptopRepository).findById((UUID) any());
        verify(this.datawarehouseService).getSingleLaptopFromDatawareHouse((UUID) any());
        verify(iterable).forEach(any());
        verify(this.calculatorService).getMWSOfLaptopFromExternalAPI((BigDecimal) any());
    }

    @Test
    @Disabled
    void testGetSingleLaptopWithInvalidID() {
        assertThrows(LaptopNotFoundException.class, () -> {
           UUID id = UUID.randomUUID();
           productService.getSingleLaptop(id);
        });
    }


    @Test
    void testDeleteLaptop() {
        doNothing().when(this.datawarehouseService).deleteLaptopInDatawareHouseDB((UUID) any());
        this.productService.deleteLaptop(UUID.randomUUID());
        verify(this.datawarehouseService).deleteLaptopInDatawareHouseDB((UUID) any());
    }

    @Test
    void testGetPriceOfLaptop() {
        Iterable<Link> iterable = (Iterable<Link>) mock(Iterable.class);
        doNothing().when(iterable).forEach(any());
        EntityModel<Laptop> entityModel = new EntityModel<>(laptop_1, iterable);

        when(this.datawarehouseService.getSingleLaptopFromDatawareHouse((UUID) any())).thenReturn(entityModel);
        assertEquals(BigDecimal.valueOf(999.99),this.productService.getPriceOfLaptop(id_1));
        verify(this.datawarehouseService).getSingleLaptopFromDatawareHouse(id_1);
        verify(iterable).forEach(any());
    }

    @Test
    void testGetMWSOfLaptop() {
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        when(this.calculatorService.getMWSOfLaptopFromExternalAPI((BigDecimal) any())).thenReturn(valueOfResult);
        BigDecimal actualMWSOfLaptop = this.productService.getMWSOfLaptop(BigDecimal.valueOf(42L));
        assertSame(valueOfResult, actualMWSOfLaptop);
        assertEquals("42", actualMWSOfLaptop.toString());
        verify(this.calculatorService).getMWSOfLaptopFromExternalAPI((BigDecimal) any());
    }
}

