package pub.ron.jwt.web;

import org.apache.shiro.authc.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pub.ron.jwt.dto.Error;

@ControllerAdvice
@ResponseBody
public class WebExceptionHandler {


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Error> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Error.from(e.getMessage()));
    }
}
