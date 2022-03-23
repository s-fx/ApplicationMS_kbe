package com.futureSheep.ApplicationMS_kbe.dataStorage;

import org.springframework.data.jpa.repository.JpaRepository;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop, UUID> {
}