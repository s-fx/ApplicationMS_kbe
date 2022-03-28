package com.futureSheep.ApplicationMS_kbe.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ContextConfiguration(classes = {CalculatorService.class})
@ExtendWith(SpringExtension.class)
class CalculatorServiceTest {
    @Autowired
    private CalculatorService calculatorService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void testGetMWSOfLaptopFromExternalAPI() throws RestClientException {
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        when(this.restTemplate.getForObject((String) any(), (Class<BigDecimal>) any(), (Object[]) any()))
                .thenReturn(valueOfResult);
        BigDecimal actualMWSOfLaptopFromExternalAPI = this.calculatorService
                .getMWSOfLaptopFromExternalAPI(BigDecimal.valueOf(42L));
        assertSame(valueOfResult, actualMWSOfLaptopFromExternalAPI);
        assertEquals("42", actualMWSOfLaptopFromExternalAPI.toString());
        verify(this.restTemplate).getForObject((String) any(), (Class<BigDecimal>) any(), (Object[]) any());
    }
}

