package com.bezkoder.spring.security.modules.login.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bezkoder.spring.security.modules.login.models.User;
import com.bezkoder.spring.security.modules.login.repository.UserRepository;

import javax.transaction.Transactional;

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

}
