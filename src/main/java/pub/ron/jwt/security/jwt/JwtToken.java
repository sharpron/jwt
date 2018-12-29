package pub.ron.jwt.security.jwt;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken {


    private final String jwtContent;
    private final String host;

    public JwtToken(String jwtContent, String host) {
        this.jwtContent = jwtContent;
        this.host = host;
    }

    @Override
    public Object getPrincipal() {
        return this.jwtContent;
    }

    public String getHost() {
        return host;
    }

    @Override
    public Object getCredentials() {
        return Boolean.TRUE;
    }
}
