package com.oasis.tasker.controllers;

import com.oasis.tasker.converters.user.UserToUserCommand;
import com.oasis.tasker.dtos.CategoryCommand;
import com.oasis.tasker.dtos.JwtCommand;
import com.oasis.tasker.dtos.SignInCommand;
import com.oasis.tasker.dtos.UserCommand;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.oasis.tasker.config.auth.TokenProvider;
import com.oasis.tasker.entities.User;
import com.oasis.tasker.services.AuthService;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private AuthService service;

  @Autowired
  private TokenProvider tokenService;

  @Autowired
  private UserToUserCommand userCommandToUser;

  @PostMapping("/signup")
  public ResponseEntity<?> signUp(@RequestBody @Valid UserCommand data) {
    service.signUp(data);
    //System.out.println(data);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/signin")
  public ResponseEntity<JwtCommand> signIn(@RequestBody @Valid SignInCommand data) {
    var emailPassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());

    var authUser = authenticationManager.authenticate(emailPassword);

    var accessToken = tokenService.generateAccessToken((User) authUser.getPrincipal());

    JwtCommand jwtCommand = new JwtCommand();
    jwtCommand.setAccessToken(accessToken);
    return ResponseEntity.ok(jwtCommand);
  }

  @GetMapping ("/user")
  public ResponseEntity<UserCommand> whomai() {

    return ResponseEntity.ok(
            new UserToUserCommand().convert(service.getLoggedInUser())
    );
  }

  @PutMapping ("/user")
  public ResponseEntity<UserCommand> updateUser(@RequestBody @Valid UserCommand data) {

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    data.setId(service.getLoggedInUser().getId());
    if (passwordEncoder.matches(data.getPassword(), service.getLoggedInUser().getPassword())) {

      return ResponseEntity.ok(
             new UserToUserCommand().convert(service.updateUser(data))
      );
    }
    else{
      System.out.println("COuld finf user");
      return ResponseEntity.notFound().build();
    }

  }
}