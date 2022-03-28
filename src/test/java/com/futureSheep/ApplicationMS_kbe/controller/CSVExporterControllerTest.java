package com.futureSheep.ApplicationMS_kbe.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;

import com.futureSheep.ApplicationMS_kbe.services.CSVExporterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {CSVExporterController.class})
@ExtendWith(SpringExtension.class)
class CSVExporterControllerTest {
    @MockBean
    private CSVExporterService cSVExporter;

    @Autowired
    private CSVExporterController cSVExporterController;

    @Test
    void testGetAllLaptopsInCsv() throws Exception {
        doNothing().when(this.cSVExporter).writeLaptopsToCSV((java.io.Writer) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/laptops/csv");
        MockMvcBuilders.standaloneSetup(this.cSVExporterController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/csv"))
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    void testGetAllLaptopsInCsv2() throws Exception {
        doNothing().when(this.cSVExporter).writeLaptopsToCSV((java.io.Writer) any());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v1/laptops/csv");
        getResult.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(this.cSVExporterController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/csv"))
                .andExpect(MockMvcResultMatchers.content().string(""));
    }
}

