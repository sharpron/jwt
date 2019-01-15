package pub.ron.jwt.security;

import org.apache.shiro.authc.AuthenticationToken;

public abstract class AuthcToken implements AuthenticationToken {

    private final String val;

    public AuthcToken(String val) {
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
