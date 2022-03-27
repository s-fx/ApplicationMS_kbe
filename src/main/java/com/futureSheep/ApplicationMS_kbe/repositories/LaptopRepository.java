package com.futureSheep.ApplicationMS_kbe.repositories;

import com.futureSheep.ApplicationMS_kbe.products.LaptopLocationOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import com.futureSheep.ApplicationMS_kbe.products.Laptop;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LaptopRepository extends JpaRepository<LaptopLocationOnly, UUID> {


}