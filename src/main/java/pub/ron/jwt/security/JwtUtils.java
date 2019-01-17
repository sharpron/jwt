package pub.ron.jwt.security;

import javax.servlet.http.HttpServletRequest;

public class JwtUtils {

    public static final String JWT_AUTH_HEADER = "Authorization";

    private JwtUtils() {
    }

    static String getAuth(HttpServletRequest servletRequest) {
        return servletRequest.getHeader(JWT_AUTH_HEADER);
    }

}
