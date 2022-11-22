package com.bezkoder.spring.security.modules.login.controllers;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/thing")
public class LostAndFound 
{
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable)
    {
        String api = "http://localhost:8090/api/thing/all";
        RestTemplate rst = new RestTemplate();
        Object[] list = rst.getForObject(api, Object[].class);

        return ResponseEntity.status(HttpStatus.OK).body( list );
    }
}
