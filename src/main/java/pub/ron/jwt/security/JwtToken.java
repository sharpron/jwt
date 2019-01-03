package pub.ron.jwt.security;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken {

    private final JwtPayload jwtPayload;

    JwtToken(JwtPayload jwtPayload) {
        this.jwtPayload = jwtPayload;
    }

    @Override
    public Object getPrincipal() {
        return jwtPayload;
    }

    @Override
    public Object getCredentials() {
        return jwtPayload;
    }
}
