package pub.ron.jwt.service.impl;

import io.jsonwebtoken.JwtException;

/**
 * 非法的jwt异常
 * @author ron
 * 2019.05.10
 */
public class IllegalJwtException extends JwtException {


    IllegalJwtException(String message) {
        super(message);
    }
}
