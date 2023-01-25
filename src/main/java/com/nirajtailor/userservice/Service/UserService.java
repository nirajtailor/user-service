package com.nirajtailor.userservice.Service;

import com.nirajtailor.userservice.Models.Response;
import com.nirajtailor.userservice.Models.User;
import com.nirajtailor.userservice.Persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Response getUsers(Long userId, String name) {
        List<User> users = userRepository.getUsers(userId, name);
        return new Response<>(users, HttpStatus.OK);
    }

    public Response createUser(User user) {
        User createdUser = userRepository.createUser(user);
        return new Response<>(createdUser, HttpStatus.CREATED);
    }

    public Response createUsers(List<User> users) {
        int row = userRepository.createUsers(users);
        return new Response<>(row, HttpStatus.CREATED);
    }
}
