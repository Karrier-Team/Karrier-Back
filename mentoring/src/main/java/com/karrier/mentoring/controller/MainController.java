package com.karrier.mentoring.controller;

import com.karrier.mentoring.auth.PrincipalDetails;
import com.karrier.mentoring.service.mail.InfoEmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin("http://localhost:3000")
@RestController
public class MainController {

    @Autowired
    InfoEmailServiceImpl infoEmailService;

    @GetMapping(value = "/")
    public String main() {
        return "Server is running";
    }

    @GetMapping(value = "/test")
    public String username() {
        infoEmailService.sendSimpleMessage("tsi0521o@gmail.com");
        return "myEmail";
    }

}
