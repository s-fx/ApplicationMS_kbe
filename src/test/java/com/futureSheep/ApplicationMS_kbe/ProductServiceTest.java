package com.futureSheep.ApplicationMS_kbe;

import com.futureSheep.ApplicationMS_kbe.exceptions.LaptopNotFoundException;
import com.futureSheep.ApplicationMS_kbe.configurations.LaptopModelAssembler;
import com.futureSheep.ApplicationMS_kbe.repositories.LaptopRepository;
import com.futureSheep.ApplicationMS_kbe.services.CalculatorService;
import com.futureSheep.ApplicationMS_kbe.services.ProductService;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import com.futureSheep.ApplicationMS_kbe.products.Location;
import com.futureSheep.ApplicationMS_kbe.services.LaptopValidationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.hateoas.EntityModel;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DataJpaTest
public class ProductServiceTest {

    @Mock
    private LaptopRepository repository;
    @Mock
    private LaptopModelAssembler assembler;
    @Mock
    private CalculatorService calculatorService;
    @Mock
    private LaptopValidationService laptopValidationService;


    private AutoCloseable autoCloseable;
    private ProductService productService;
    private final String ID_STRING = "ed759a38-f002-403d-bfc2-2bd0ab88ee10";


/*    @BeforeEach
    void setUp(){
        // initialise all Mocks in this Test class
        autoCloseable = MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl(repository,assembler,calculatorService,laptopValidationService);
    }*/

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }


    @Test
    void collectAllLaptopsTest(){
        // when
        productService.collectAllLaptops();
        // then
        verify(repository).findAll();
    }


    @Test
    void validateLaptopBeforeSavingIntoDBTest() {
        Laptop laptop = new Laptop(UUID.randomUUID(), "DELL", BigDecimal.valueOf(9.9),55.4, new Location(52.521992, 13.413244));

        productService.validateAndSaveLaptop(laptop);

        verify(assembler).toModel(laptop);
        verify(laptopValidationService).addLaptop(laptop);
    }

    @Test
    @Deprecated
    void saveLaptopIntoDBTest() {
        Laptop laptop = new Laptop(UUID.randomUUID(), "DELL", BigDecimal.valueOf(9.9),55.4, new Location(52.521992, 13.413244));
        productService.saveLaptopIntoDB(laptop);

        ArgumentCaptor<Laptop> argumentCaptor = ArgumentCaptor.forClass(Laptop.class);
        //verify(repository).save(argumentCaptor.capture());

        Laptop capturedLaptop = argumentCaptor.getValue();
        assertThat(capturedLaptop).isEqualTo(laptop);
    }

    @Test
    void getSingleLaptopThrowsExceptionTest(){
        UUID id = UUID.randomUUID();

        Exception exception = assertThrows(LaptopNotFoundException.class, () -> {
            productService.getSingleLaptop(id);
        });

        String exceptedMessage = "Could not find Laptop " + id;
        String actualMessage = exception.getMessage();

        assertEquals(exceptedMessage,actualMessage);
    }

    @Test
    @Deprecated
    void getSingleLaptopSuccessTest(){
        // given
        UUID id = UUID.fromString(ID_STRING);
        Laptop laptop = new Laptop(UUID.randomUUID(),"DELL", BigDecimal.valueOf(9.9),55.4, new Location(52.521992, 13.413244));
        laptop.setId(id);
        EntityModel<Laptop> laptopEntityModel = assembler.toModel(laptop);


        //given(repository.findById(UUID.fromString(ID_STRING))).willReturn(Optional.of(laptop));

        // when
        EntityModel<Laptop> laptopEntityModel_actual = productService.getSingleLaptop(UUID.fromString(ID_STRING));

        // then
        assertEquals(laptopEntityModel, laptopEntityModel_actual);
    }

    @Test
    void deleteLaptopByIDThrowsException(){
        UUID id = UUID.randomUUID();
        Exception exception = assertThrows(LaptopNotFoundException.class, () -> {
            productService.deleteLaptop(id);
        });

        String expectedMessage = "Could not find Laptop " + id;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage,actualMessage);
    }

    @Test
    void getPriceOfLaptopThrowsException(){
        UUID id = UUID.randomUUID();
        Exception exception = assertThrows(LaptopNotFoundException.class, () -> {
            productService.getPriceOfLaptop(id);
        });

        String expectedMessage = "Could not find Laptop " + id;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage,actualMessage);
    }

    @Test
    @Deprecated
    void getPriceOfLaptopTest(){
        UUID id = UUID.fromString(ID_STRING);
        Laptop laptop = new Laptop(UUID.randomUUID(),"DELL", BigDecimal.valueOf(9.9),55.4, new Location(52.521992, 13.413244));
        laptop.setId(id);
        EntityModel<Laptop> laptopEntityModel = assembler.toModel(laptop);


        //given(repository.findById(UUID.fromString(ID_STRING))).willReturn(Optional.of(laptop));

        BigDecimal actualPrice = productService.getPriceOfLaptop(UUID.fromString(ID_STRING));
        assertEquals(actualPrice, laptop.getPrice());
    }

}
