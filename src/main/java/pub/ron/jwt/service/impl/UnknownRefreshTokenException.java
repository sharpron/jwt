package pub.ron.jwt.service.impl;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 未知的刷新token异常，证明token不存在
 * @author ron
 * 2019.01.17
 */
class UnknownRefreshTokenException extends AuthenticationException {

    UnknownRefreshTokenException(String message) {
        super(message);
    }
}
