package com.bezkoder.spring.security.modules.login.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.security.modules.login.models.Role;
import com.bezkoder.spring.security.modules.login.models.ERole;
import com.bezkoder.spring.security.modules.login.models.User;

import com.bezkoder.spring.security.modules.login.payload.request.SignupRequest;

import com.bezkoder.spring.security.modules.login.repository.RoleRepository;
import com.bezkoder.spring.security.modules.login.repository.UserRepository;

import com.bezkoder.spring.security.modules.login.services.UserService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController 
{

  @Autowired
  UserService userService;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  UserRepository userRepository;
  
  @GetMapping("/all")
  @PreAuthorize("hasRole('ROOT')")
  public ResponseEntity<List<User>> getAllUsers(@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable)
  {
      return ResponseEntity.status(HttpStatus.OK).body(  userService.findAll() );
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ROOT')")
  public ResponseEntity<Object> deletUser(@PathVariable(value = "id") long id)
  {
      Optional<User> user = userService.findById( id );
      
      if (!user.isPresent())
      {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found.");
      }

      userService.delete(user.get());

      return ResponseEntity.status(HttpStatus.OK).body("user deleted successfully.");
  }

  @DeleteMapping("/all")
  @PreAuthorize("hasRole('ROOT')")
  public ResponseEntity<Object> deletAllUsers()
  {
      List<User> user = userService.findAll();
      userService.deleteAll(user);
      
      return ResponseEntity.status(HttpStatus.OK).body("user's deleted successfully.");
  }


  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ROOT')")
  public ResponseEntity<Object> getUserById(@PathVariable(value = "id") long id)
  {
      Optional<User> user = userService.findById( id );

      if (!user.isPresent()) 
      {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found.");
      }
      
      return ResponseEntity.status(HttpStatus.OK).body(user.get());
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ROOT')")
  public ResponseEntity<Object> registerUser(@PathVariable(value = "id") long id, @Valid @RequestBody SignupRequest signUpRequest) 
  {
      Optional<User> user = userService.findById( id );

      if (!user.isPresent()) 
      {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found.");
      }

      Set<String> strRoles = signUpRequest.getRoles();
      Set<Role> roles = new HashSet<>();

      User userObj = new User();
      BeanUtils.copyProperties(signUpRequest, userObj);
      userObj.setId(user.get().getId());
  
      if ( !strRoles.isEmpty() ) 
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

        userObj.setRoles( roles );
        
      }  
      else 
      {
         userObj.setRoles( user.get().getRoles() );
      }

      try 
      {
          return ResponseEntity.status(HttpStatus.OK).body( userService.save( userObj ));
      } 
      catch (Exception e) 
      {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( "date not valid" );
      }

  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public String userAccess() 
  {
      return "User Content.";
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() 
  {
      return "Moderator Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess()
  {
      return "Admin Board.";
  }
}
