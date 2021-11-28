package com.futureSheep.ApplicationMS_kbe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("CalculatorService")
public class CalculatorService {

    @Autowired
    private RestTemplate restTemplate;

    private final String CALC_API = "http://localhost:8081/api/calculator";



    public Double getMWSOfLaptopFromExternalAPI(double price) {
        String url = CALC_API + "/{price}";
        return restTemplate.getForObject(url, Double.class, price);
    }


}
