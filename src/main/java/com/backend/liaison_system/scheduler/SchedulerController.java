package com.backend.liaison_system.scheduler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "liaison/api/v1/schedule")
public class SchedulerController {

    @GetMapping
    public ResponseEntity<String> keepServerAlive() {
        return new ResponseEntity<>("connected!", HttpStatus.OK);
    }
}
