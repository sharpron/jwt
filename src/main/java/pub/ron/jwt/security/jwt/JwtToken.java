package pub.ron.jwt.security.jwt;

import pub.ron.jwt.security.AuthcToken;

/**
 * jwt认证token
 * @author ron
 * 2019.01.17
 */
class JwtToken extends AuthcToken {

    JwtToken(String val) {
        super(val);
    }
}
