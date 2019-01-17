package pub.ron.jwt.security;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author ron
 * 2019.01.17
 * shiro认证token
 */
public abstract class AuthcToken implements AuthenticationToken {

    private final String val;

    AuthcToken(String val) {
        this.val = val;
    }

    @Override
    public Object getPrincipal() {
        return val;
    }

    @Override
    public Object getCredentials() {
        return val;
    }
}
