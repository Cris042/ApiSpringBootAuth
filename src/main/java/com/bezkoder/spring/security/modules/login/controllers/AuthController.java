package com.bezkoder.spring.security.modules.login.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.security.modules.login.models.ERole;
import com.bezkoder.spring.security.modules.login.models.Role;
import com.bezkoder.spring.security.modules.login.models.User;

import com.bezkoder.spring.security.modules.login.payload.request.LoginRequest;
import com.bezkoder.spring.security.modules.login.payload.request.SignupRequest;
import com.bezkoder.spring.security.modules.login.payload.response.MessageResponse;
import com.bezkoder.spring.security.modules.login.payload.response.UserInfoResponse;

import com.bezkoder.spring.security.modules.login.repository.RoleRepository;

import com.bezkoder.spring.security.modules.login.security.jwt.JwtUtils;
import com.bezkoder.spring.security.modules.login.security.services.UserDetailsImpl;
import com.bezkoder.spring.security.modules.login.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController 
{
  
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserService userService;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
  {

      Authentication authentication = authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

      List<String> roles = userDetails.getAuthorities().stream()
          .map(item -> item.getAuthority())
          .collect(Collectors.toList());

      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
          .body(  new UserInfoResponse( userDetails.getId(),userDetails.getUsername(),userDetails.getEmail(),roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) 
  {
      if (userService.existsByUsername( signUpRequest.getUsername()) ) 
      {
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
      }

      if (userService.existsByEmail( signUpRequest.getEmail()) ) 
      {
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
      }

      // Create new user's account
      User user = new User
      (
        signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getName(),signUpRequest.getAthletic(),
        signUpRequest.getCourse(), signUpRequest.getInsta(), signUpRequest.getNumber(), signUpRequest.getStatus(), signUpRequest.getProfilePicture()              
      );

      Set<String> strRoles = signUpRequest.getRoles();
      Set<Role> roles = new HashSet<>();

      if (strRoles == null) 
      {
          Role userEvent = new Role(ERole.ROLE_EVENT_USER);
          Role userLost = new Role(ERole.ROLE_LOST_USER);
          Role userRide = new Role(ERole.ROLE_RIDE_USER);

          roles.add(userEvent); 
          roles.add(userLost); 
          roles.add(userRide);   
      } 
      else 
      {
        strRoles.forEach(role -> 
        {
          switch (role) 
          {
            case "root":  
              Role root = new Role(ERole.ROLE_ROOT);
              roles.add( root );
              break;

            case "adminEvent":  
              Role adminEvent = new Role(ERole.ROLE_EVENT_ADMIN);
              roles.add(adminEvent);
              break;

            case "modEvent":
              Role modEvent = new Role(ERole.ROLE_EVENT_MOD);
              roles.add(modEvent);
              break;
            
            case "userEvent":
              Role userEvent = new Role(ERole.ROLE_EVENT_USER);
              roles.add(userEvent);
              break;
            
            case "adminLost":  
              Role adminLost = new Role(ERole.ROLE_LOST_ADMIN);
              roles.add(adminLost);
              break;

            case "modLost":
              Role modLost = new Role(ERole.ROLE_LOST_MOD);
              roles.add(modLost);
              break;
            
            case "userLost":
              Role userLost = new Role(ERole.ROLE_LOST_USER);
              roles.add(userLost);
              break;
            
            case "adminRide":  
              Role adminRide = new Role(ERole.ROLE_RIDE_ADMIN);
              roles.add(adminRide);
              break;

            case "modRide":
              Role modRide = new Role(ERole.ROLE_RIDE_MOD);
              roles.add(modRide);
              break;
            
            case "userRide":
              Role userRide = new Role(ERole.ROLE_RIDE_USER);
              roles.add(userRide);
              break;
            
             
            default: 
              Role userDefaltEvent = new Role(ERole.ROLE_EVENT_USER);
              Role userDefaltLost = new Role(ERole.ROLE_LOST_USER);
              Role userDefaltRide = new Role(ERole.ROLE_RIDE_USER);     

              roles.add(userDefaltEvent); 
              roles.add(userDefaltLost); 
              roles.add(userDefaltRide);   
          }
        });
    }

    user.setRoles(roles);
    userService.save(user);

    return ResponseEntity.ok( user );
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() 
  {
      ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
          .body(new MessageResponse("You've been signed out!"));
  }
}
