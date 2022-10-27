package com.bezkoder.spring.security.modules.login.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bezkoder.spring.security.modules.login.models.ERole;
import com.bezkoder.spring.security.modules.login.models.Role;
import com.bezkoder.spring.security.modules.login.models.User;
import com.bezkoder.spring.security.modules.login.repository.UserRepository;

import javax.transaction.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService 
{
    @Autowired
    UserRepository userRepository;
    
    @Transactional
    public User save(User User) 
    {
        return userRepository.save(User);
    }

    public Optional<User> findByUsername(String username) 
    {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(long id) 
    {
        return userRepository.findById(id);
    }

    public Boolean existsByEmail(String email)
    {
        return userRepository.existsByEmail(email);
    }

    public Boolean existsByUsername(String username)
    {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public void delete(User UserModel) 
    {
        userRepository.delete(UserModel);
    }

    public List<User> findAll()
    {
        return userRepository.findAll();
    }

    public void deleteAll(List<User> user)
    {
        List<User> allUser = userRepository.findAll();
        userRepository.deleteAll( allUser);
    }

    public Set<String> getRoles( Set<String> roles )
    {
        return roles;
    }

    public Set<Role> roleUser( Set<String> strRoles )
    {
        Set<Role> roles = new HashSet<>();

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

        return roles;
    }

}
