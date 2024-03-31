package com.navin.user.service.services.impl;

import com.navin.user.service.entities.Rating;
import com.navin.user.service.entities.User;
import com.navin.user.service.exceptions.ResourceNotFoundException;
import com.navin.user.service.repositories.UserRepository;
import com.navin.user.service.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        ArrayList<Rating> ratingOfUser = restTemplate.getForObject("http://localhost:8083/ratings/users/"+user.getUserId(), ArrayList.class);
        user.setRatings(ratingOfUser);
        logger.info("{}",ratingOfUser);
        return user;
    }
}
