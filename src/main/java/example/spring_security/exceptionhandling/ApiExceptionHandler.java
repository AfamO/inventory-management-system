package example.spring_security.exceptionhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;
import java.net.http.HttpResponse;

@ControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)

public class ApiExceptionHandler {
    private static String EXCEPTION_WAS_THROWN = "Exception was thrown::";

    @ExceptionHandler({AuthenticationException.class, InvalidBearerTokenException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<String> handleInvalidBearerTokenException
            (AuthenticationException authException) {
        log.error(EXCEPTION_WAS_THROWN, authException);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                body("Authentication Failed: Invalid Token-"+authException.getMessage());
    }
}
