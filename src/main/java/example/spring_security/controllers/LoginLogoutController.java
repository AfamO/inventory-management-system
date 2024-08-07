package example.spring_security.controllers;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginLogoutController {
    @GetMapping("/login")
    String login() {
        return "login";
    }

    @GetMapping("/app/user/logout")
    String logout() {
       return "logout";
    }
}
