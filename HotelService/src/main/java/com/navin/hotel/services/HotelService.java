package com.navin.hotel.services;

import com.navin.hotel.entities.Hotel;

import java.util.List;

public interface HotelService {

    //create
    Hotel create (Hotel hotel);

    //get all
    List<Hotel> getAll();

    //get single
    Hotel get (String id);

}
