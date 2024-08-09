package example.spring_security.events;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationEventsListener {
    @EventListener
    public void onSuccess(AuthenticationSuccessEvent authenticationSuccessEvent) {
        log.info("Authentication Event successful. Authorities:: {}", authenticationSuccessEvent.getAuthentication().getAuthorities());

    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent authenticationFailureEvent) {
        log.info("Authentication Event Failed. Throwable:: {}, Message::{}", authenticationFailureEvent.getException().fillInStackTrace(),
                authenticationFailureEvent.getException().getLocalizedMessage());
    }

}
