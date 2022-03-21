package com.futureSheep.ApplicationMS_kbe.calculatorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ContextConfiguration(classes = {CalculatorServiceImpl.class})
@ExtendWith(SpringExtension.class)
class CalculatorServiceImplTest {
    @Autowired
    private CalculatorServiceImpl calculatorServiceImpl;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void testGetMWSOfLaptopFromExternalAPI() throws RestClientException {
        when(this.restTemplate.getForObject((String) any(), (Class<Double>) any(), (Object[]) any())).thenReturn(10.0d);
        assertEquals(10.0d, this.calculatorServiceImpl.getMWSOfLaptopFromExternalAPI(10.0d).doubleValue());
        verify(this.restTemplate).getForObject((String) any(), (Class<Double>) any(), (Object[]) any());
    }
}

