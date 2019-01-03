package pub.ron.jwt.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class JwtTokenizer {

    private static final String SECRET = "42340ag0as8gfs8ay2hi24j091fd9f113";

    private static final int ACTIVE_INTERVAL = 60 * 1000;

    private static final int EXPIRED_REFRESH_INTERVAL = 60 * 60 * 1000;

    private static final String JWT_AUTH_FLAG = "Bearer ";

    private static final String JWT_AUTH_HEADER = "Authorization";

    private JwtTokenizer() {
    }


    public static String createToken(JwtPayload payload)
        throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {
        String jwt = Jwts.builder()
                .setId(payload.getId())
                .setIssuer("me")
                .setAudience("user")
                .setSubject("app")
                .setIssuedAt(new Date(payload.getTime()))
                .setExpiration(new Date(payload.getTime() + ACTIVE_INTERVAL))
                .claim("host", payload.getHost())
                .claim("roles", payload.getRoles())
                .claim("perms", payload.getPerms())
                .compressWith(CompressionCodecs.GZIP)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
        return JWT_AUTH_FLAG + jwt;
    }

    static boolean couldRefresh(JwtPayload jwtPayload) {
        return System.currentTimeMillis() - jwtPayload.getTime() < EXPIRED_REFRESH_INTERVAL;
    }

    static boolean useJwtAuth(HttpServletRequest servletRequest) {
        String auth = getAuth(servletRequest);
        return StringUtils.isNotBlank(auth) && auth.startsWith(JWT_AUTH_FLAG);
    }

    static void addAuthToResponseHeader(HttpServletResponse response, String token) {
        response.setHeader(JWT_AUTH_HEADER, token);
    }

    static String getAuth(HttpServletRequest servletRequest) {
        return servletRequest.getHeader(JWT_AUTH_HEADER);
    }


    static JwtPayload parse(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .parseClaimsJws(token.substring(JWT_AUTH_FLAG.length()))
                .getBody();
        return parseClaims(claims);
    }

    @SuppressWarnings("unchecked")
    static JwtPayload parseClaims(Claims claims) {
        return new JwtPayload(claims.getId(),
                claims.getIssuedAt().getTime(),
                claims.get("host", String.class),
                claims.get("roles", List.class),
                claims.get("perms", List.class));
    }
}
