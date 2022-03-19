package com.futureSheep.ApplicationMS_kbe.calculatorService;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@CommonsLog
@Service("CalculatorService")
public class CalculatorServiceImpl implements CalculatorService {

    @Autowired
    private RestTemplate restTemplate;

    private final String CALCULATOR_API = "http://localhost:8081/api/v1/calculator";


    @Override
    public Double getMWSOfLaptopFromExternalAPI(double price) {
        log.info("External Api at " + CALCULATOR_API + " is called with price value: " + price);
        String url = CALCULATOR_API + "/{price}";
        return restTemplate.getForObject(url, Double.class, price);
    }


}
