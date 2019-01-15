package pub.ron.jwt.service;

import org.apache.shiro.authc.AuthenticationException;

class UnknownRefreshTokenException extends AuthenticationException {

    UnknownRefreshTokenException(String message) {
        super(message);
    }
}
