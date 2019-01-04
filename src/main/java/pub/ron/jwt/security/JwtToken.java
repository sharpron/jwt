package pub.ron.jwt.security;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken {

    private final String jwt;

    JwtToken(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public Object getPrincipal() {
        return jwt;
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }
}
