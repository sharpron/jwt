package pub.ron.jwt.security.hmac;

import org.apache.shiro.authc.AuthenticationToken;

public class HmacToken implements AuthenticationToken {

    private static final int ONE_MINUTE = 10 * 60 * 1000;

    private final String username;

    private final String digest;

    private final long timestamp;

    private final String host;

    HmacToken(String username,
                     String digest,
                     long timestamp,
                     String host) {
        this.username = username;
        this.digest = digest;
        this.timestamp = timestamp;
        this.host = host;

    }

    @Override
    public String getPrincipal() {
        return this.username;
    }

    @Override
    public Object getCredentials() {
        return Boolean.TRUE;
    }

    String getDigest() {
        return digest;
    }

    String getHost() {
        return host;
    }

    boolean expired() {
        return System.currentTimeMillis() - timestamp > ONE_MINUTE;
    }


    String getSignature() {
        return username + timestamp + host;
    }
}
