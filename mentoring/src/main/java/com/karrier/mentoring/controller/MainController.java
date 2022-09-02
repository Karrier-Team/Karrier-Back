package com.karrier.mentoring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin("http://localhost:3000")
@Controller
public class MainController {

    @GetMapping(value = "/")
    public ResponseEntity<Object> main() {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
