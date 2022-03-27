package com.futureSheep.ApplicationMS_kbe.exceptions;

import java.util.UUID;

public class LaptopNotFoundException extends RuntimeException {

    public LaptopNotFoundException(UUID id) {
        super("Could not find Laptop " + id);
    }
}
