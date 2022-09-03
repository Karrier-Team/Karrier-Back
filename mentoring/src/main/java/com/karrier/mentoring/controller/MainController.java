package com.karrier.mentoring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MainController {

    @GetMapping(value = "/")
    public String main() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal==null)
            return "No USER";
        else {
            String email = ((UserDetails) principal).getUsername();
            return email;
        }
    }

}
