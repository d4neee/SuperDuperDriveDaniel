package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    private UserMapper userMapper;
    private HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    // Does the user currently exist in the database?
    public boolean isUsernameAvailable(String username) {
        return userMapper.getUser(username) == null;
    }

    public User getUser(String username) {
        return userMapper.getUser(username);
    }

    public int getLoggedInUserId(Authentication auth) {
        String username = auth.getName();
        return getUser(username).getUserId();
    }

    /*
     * insert the user into the database
     * after hashing the password.
     */
    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);

        // setup the new user object
        User newUser = new User(null, user.getUsername(), encodedSalt,
                hashedPassword, user.getFirstName(), user.getLastName());

        return userMapper.insertUser(newUser);

    }

}
