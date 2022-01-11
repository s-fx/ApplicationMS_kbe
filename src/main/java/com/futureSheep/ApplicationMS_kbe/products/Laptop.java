package com.futureSheep.ApplicationMS_kbe.products;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "laptopdb")
public class Laptop {

    @Id @GeneratedValue
    @Column(name = "id", updatable = false)
    private UUID id;
    @Column(name = "brand", nullable = false, columnDefinition = "TEXT")
    @NotNull(message = "Brand name must be betweenn 3 and 20 characters")
    @Size(min = 1, max = 20)
    private String brand;
    @Column(name = "price", nullable = false)
    @NotNull(message = "Price can not be 0")
    private double price;
    //@Column(name = "weight", nullable = false)
    //@NotNull(message = "Weight can not be 0")
    @Positive(message = "Weight must be positive")
    private double weight;
    private Location location;

    private double mehrwertsteuer;

    public Laptop() {};

    public Laptop(String brand, double price, double weight, Location location) {
        this.brand = brand;
        this.price = price;
        this.weight = weight;
        this.location = location;
        this.mehrwertsteuer = 0.0;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Laptop{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Laptop laptop = (Laptop) o;
        return Double.compare(laptop.price, price) == 0 && Double.compare(laptop.weight, weight) == 0 && Objects.equals(id, laptop.id) && Objects.equals(brand, laptop.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, price, weight);
    }

    public double getMehrwertsteuer() {
        return mehrwertsteuer;
    }

    public void setMehrwertsteuer(double mehrwertsteuer) {
        this.mehrwertsteuer = mehrwertsteuer;
    }
}
