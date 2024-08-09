package example.spring_security.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
@Slf4j
public class WelcomeController {

    @GetMapping("/welcome")
    public String welcome (HttpServletRequest httpServletRequest) {

        Authentication auth = (Authentication) httpServletRequest.getUserPrincipal();
        log.info("The LoggedIn User is == {} and Principal from SecurityContext== {}",
                SecurityContextHolder.getContext().getAuthentication().getName(), auth.getPrincipal());
        log.info("The LoggedIn User=={}", httpServletRequest.getRemoteUser());
        boolean isRoleUser = httpServletRequest.isUserInRole("USER");
        log.info("Is the user in Role 'USER'== {}", isRoleUser);
        auth = SecurityContextHolder.getContext().getAuthentication();

        return "Hi "+auth.getName()+",Welcome to my spring security app";
    }

    @GetMapping("/csrf")
    public CsrfToken getCSRFToken(CsrfToken csrfToken) {
        return csrfToken;
    }
}
