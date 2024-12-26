package org.cws.web.software.engineer.task.security.test.controller;

import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class SecurityTestController {

    public SecurityTestController() {
        LoggerFactory.getLogger(SecurityTestController.class).info("creates security test controller");
    }

    @GetMapping
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("Authenticated");
    }

}
