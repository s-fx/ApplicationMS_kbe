package com.futureSheep.ApplicationMS_kbe.calculatorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("CalculatorService")
public class CalculatorServiceImpl implements CalculatorService {

    @Autowired
    private RestTemplate restTemplate;

    private final String CALCULATOR_API = "http://localhost:8081/api/calculator";


    @Override
    public Double getMWSOfLaptopFromExternalAPI(double price) {
        String url = CALCULATOR_API + "/{price}";
        return restTemplate.getForObject(url, Double.class, price);
    }


}
