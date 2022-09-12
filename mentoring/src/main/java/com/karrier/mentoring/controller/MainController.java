package com.karrier.mentoring.controller;

import com.karrier.mentoring.service.mail.ProgramInfoEmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin("http://localhost:3000")
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
        infoEmailService.sendSimpleMessage("tsi0521o@gmail.com");
        return "myEmail";
    }

}
