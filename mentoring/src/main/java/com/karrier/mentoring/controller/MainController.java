package com.karrier.mentoring.controller;

import com.karrier.mentoring.auth.PrincipalDetails;
import com.karrier.mentoring.service.mail.ProgramInfoEmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin({"http://localhost:3000", "https://web-reactapp-48f224l75lf6ut.gksl1.cloudtype.app/", "https://www.karrier.co.kr/"})
@RestController
public class MainController {

    @Autowired
    ProgramInfoEmailServiceImpl infoEmailService;

    @GetMapping(value = "/")
    public String main() {
        return "Server is running";
    }

    @GetMapping(value = "/test")
    public String username() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((PrincipalDetails) principal).getUsername();
        return email;
    }

}
