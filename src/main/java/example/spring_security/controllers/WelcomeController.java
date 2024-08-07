package example.spring_security.controllers;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class WelcomeController {

    @GetMapping("/welcome")
    public String welcome () {
        return "Welcome to my spring security app";
    }

    @GetMapping("/csrf")
    public CsrfToken getCSRFToken(CsrfToken csrfToken) {
        return csrfToken;
    }
}
