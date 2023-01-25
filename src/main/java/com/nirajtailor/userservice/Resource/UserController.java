package com.nirajtailor.userservice.Resource;

import com.nirajtailor.userservice.Models.Response;
import com.nirajtailor.userservice.Models.User;
import com.nirajtailor.userservice.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getUsers",
            method = RequestMethod.GET)
    public ResponseEntity<Object> getUsers(
            @RequestHeader(value = "version", required=false) String version,
            @RequestHeader(value = "source", required=false) String source,
            @RequestParam(value = "id", required = false) Long userId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "mobile", required = false) String mobile
    ){
        Response response = userService.getUsers(userId, name);
        return new ResponseEntity<>(response.getBody(), response.getStatus());
    }

    @RequestMapping(value = "/user",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createUser(
            @RequestBody User user
    ){
        Response response = userService.createUser(user);
        return new ResponseEntity<>(response.getBody(), response.getStatus());
    }


    @RequestMapping(value = "/users",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createUsers(
            @RequestBody List<User> users
    ){
        Response response = userService.createUsers(users);
        return new ResponseEntity<>(response.getBody(), response.getStatus());
    }

}
