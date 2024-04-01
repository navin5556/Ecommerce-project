package com.navin.user.service.services.impl;

import com.navin.user.service.entities.Hotel;
import com.navin.user.service.entities.Rating;
import com.navin.user.service.entities.User;
import com.navin.user.service.exceptions.ResourceNotFoundException;
import com.navin.user.service.repositories.UserRepository;
import com.navin.user.service.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }


    //get single user
    @Override
    public User getUser(String userId) {
        //get user from database with the help of user repository
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User with given ID is not found on server !! : "+userId));

        //fetch rating of the above user from RATING SERVICE
        //http://localhost:8083/ratings/users/4d1eed31-d658-4c61-8f84-d60fa2ccb889
        // ArrayList<Rating> ratingOfUser = restTemplate.getForObject("http://localhost:8083/ratings/users/"+user.getUserId(), ArrayList.class);
        //fetch rating of the above user from RATING SERVICE
        //http://localhost:8083/ratings/users/4d1eed31-d658-4c61-8f84-d60fa2ccb889
        Rating[] ratingOfUser = restTemplate.getForObject("http://RATINGSERVICE/ratings/users/"+user.getUserId(), Rating[].class);

        List<Rating> ratings = Arrays.stream(ratingOfUser).toList();

        List<Rating> ratingList = ratings.stream().map(rating -> {
            //api call to hotel service to get the hotel
            // http://localhost:8082/hotels/aee1f027-83ec-4701-8930-4106724b949c
            Hotel hotel = restTemplate.getForObject("http://HOTELSERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            rating.setHotel(hotel);
            return rating;
        }).collect(Collectors.toList());


        // Print the type of ratingOfUser
        logger.info("Type of ratingOfUser: {}", ratingOfUser.getClass().getSimpleName());
        // user.setRatings(ratingOfUser);
        user.setRatings(ratingList);
        logger.info("{}",ratingList);
        return user;
    }
}
