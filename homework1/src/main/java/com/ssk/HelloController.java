package com.ssk;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    public ResponseEntity<String> hello() {
        String msg = "Hello World!";
        return ResponseEntity.ok(msg);
    }
}