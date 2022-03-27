package com.futureSheep.ApplicationMS_kbe.csvExporter;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.futureSheep.ApplicationMS_kbe.services.CSVExporter;
import com.futureSheep.ApplicationMS_kbe.services.ProductService;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import com.futureSheep.ApplicationMS_kbe.products.Location;

import java.io.StringWriter;
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

@ContextConfiguration(classes = {CSVExporter.class})
@ExtendWith(SpringExtension.class)
class CSVExporterImplTest {
    @Autowired
    private CSVExporter cSVExporterImpl;

    @MockBean
    private ProductService productService;

    @Test
    void testWriteLaptopsToCSV() {
        when(this.productService.collectAllLaptops()).thenReturn(new ArrayList<>());
        this.cSVExporterImpl.writeLaptopsToCSV(new StringWriter());
        verify(this.productService).collectAllLaptops();
    }

    @Test
    void testWriteLaptopsToCSV2() {
        Iterable<Link> iterable = (Iterable<Link>) mock(Iterable.class);
        doNothing().when(iterable).forEach(any());
        EntityModel<Laptop> e = new EntityModel<>(new Laptop(), iterable);

        ArrayList<EntityModel<Laptop>> entityModelList = new ArrayList<>();
        entityModelList.add(e);
        when(this.productService.collectAllLaptops()).thenReturn(entityModelList);
        this.cSVExporterImpl.writeLaptopsToCSV(new StringWriter());
        verify(this.productService).collectAllLaptops();
        verify(iterable).forEach(any());
    }

    @Test
    void testWriteLaptopsToCSV3() {
        Iterable<Link> iterable = (Iterable<Link>) mock(Iterable.class);
        doNothing().when(iterable).forEach(any());
        EntityModel<Laptop> e = new EntityModel<>(
                new Laptop(UUID.randomUUID(), "HP", BigDecimal.valueOf(9.9), 10.0d, new Location()), iterable);

        ArrayList<EntityModel<Laptop>> entityModelList = new ArrayList<>();
        entityModelList.add(e);
        when(this.productService.collectAllLaptops()).thenReturn(entityModelList);
        this.cSVExporterImpl.writeLaptopsToCSV(new StringWriter());
        verify(this.productService).collectAllLaptops();
        verify(iterable).forEach(any());
    }

    @Test
    void testWriteLaptopsToCSV4(){
        ArrayList<EntityModel<Laptop>> entityModelList = new ArrayList<>();
        Laptop laptop_1 = new Laptop(UUID.randomUUID(),"HP", BigDecimal.valueOf(9.9),55.4, new Location(52.521992, 13.413244));
        Laptop laptop_2 = new Laptop(UUID.randomUUID(), "Dell", BigDecimal.valueOf(9.9),45.4, new Location(50.521992, 13.413244));
        entityModelList.add(new EntityModel<>(laptop_1));
        entityModelList.add(new EntityModel<>(laptop_2));
        when(this.productService.collectAllLaptops()).thenReturn(entityModelList);
        this.cSVExporterImpl.writeLaptopsToCSV(new StringWriter());
        verify(this.productService).collectAllLaptops();

    }

}

