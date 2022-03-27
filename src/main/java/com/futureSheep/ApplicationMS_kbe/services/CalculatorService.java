package com.futureSheep.ApplicationMS_kbe.services;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@CommonsLog
@Service("CalculatorService")
public class CalculatorService {

    @Autowired
    private RestTemplate restTemplate;

    private final String CALCULATOR_API = "http://localhost:8085/api/v1/calculator";


    public BigDecimal getMWSOfLaptopFromExternalAPI(BigDecimal price) {
        log.info("External Api at " + CALCULATOR_API + " is called with price value: " + price);
        String url = CALCULATOR_API + "/{price}";
        return restTemplate.getForObject(url, BigDecimal.class, price);
    }


}
