package com.futureSheep.ApplicationMS_kbe.products;


import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "laptopdb")
public class LaptopLocationOnly {

    @Id
    @Column(name = "id", updatable = false)
    private UUID id;
    @Column(name = "location", nullable = false)
    private Location location;

    public LaptopLocationOnly(UUID id, Location location) {
        this.id = id;
        this.location = location;
    }

    public LaptopLocationOnly() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
