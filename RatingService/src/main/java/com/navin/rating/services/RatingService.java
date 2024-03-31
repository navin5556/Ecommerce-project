package com.navin.rating.services;

import com.navin.rating.entities.Rating;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RatingService {

    //create
    Rating create(Rating rating);


    //get all ratings
    List<Rating> getRatings();


    //get all by userId
    List<Rating> getRatingByUserId(String userId);


    //get all by hotel
    List<Rating> getRatingByHotelId(String hotelId);
}
