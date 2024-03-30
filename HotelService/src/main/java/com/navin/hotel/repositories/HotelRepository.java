package com.navin.hotel.repositories;

import com.navin.hotel.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel,String> {
}

//start with 1:32