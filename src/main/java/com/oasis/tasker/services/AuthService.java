package com.oasis.tasker.services;

import com.oasis.tasker.dtos.UserCommand;
import com.oasis.tasker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.oasis.tasker.entities.User;
import com.oasis.tasker.exceptions.InvalidJwtException;

@Service
public class AuthService implements UserDetailsService {

  @Autowired
  UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    var user = repository.findByEmail(username);
    return user;
  }

  public UserDetails signUp(UserCommand data) throws InvalidJwtException {

    if (repository.findByEmail(data.getEmail()) != null) {
      throw new InvalidJwtException("Username already exists");
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());

    User newUser = new User(data.getEmail(), encryptedPassword, data.getFirst_name(), data.getLast_name(), data.getRole());

    return repository.save(newUser);

  }

  public User updateUser(UserCommand data) throws InvalidJwtException {

    String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword_confirm());

    User updatedUser = new User(data.getEmail(), encryptedPassword, data.getFirst_name(), data.getLast_name(), data.getRole());
    return repository.findById(data.getId())
            .map(usr -> {
              usr.setPassword(encryptedPassword);
              usr.setFirst_name(data.getFirst_name());
              usr.setLast_name(data.getLast_name());
              return repository.save(usr);
            })
            .orElseGet(() ->{
              return updatedUser; //if by any omission id is not found, make no changes. Lol
            });

  }


  public User getLoggedInUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      return repository.findByEmail(authentication.getName()); // Assuming the email is stored as the principal's name
    }
    return null; // No authenticated user
  }
}