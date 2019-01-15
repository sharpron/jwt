package pub.ron.jwt.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import pub.ron.jwt.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class JwtUtils {

    private static final String SECRET = "42340ag0as8gfs8ay2hi24j091fd9f113";

    private static final int ACTIVE_INTERVAL =  60 * 1000;

    private static final String JWT_AUTH_FLAG = "Bearer ";

    public static final String JWT_AUTH_HEADER = "Authorization";

    private JwtUtils() {
    }

    public static String createToken(User user) {
        return createToken(new JwtPayload(user));
    }


    public static String createToken(JwtPayload payload)
        throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {
        String jwt = io.jsonwebtoken.Jwts.builder()
                .setId(payload.getId())
                .setIssuer("me")
                .setAudience("user")
                .setSubject("app")
                .setIssuedAt(new Date(payload.getTime()))
                .setExpiration(new Date(payload.getTime() + ACTIVE_INTERVAL))
                .claim("roles", payload.getRoles())
                .claim("perms", payload.getPerms())
                .compressWith(CompressionCodecs.GZIP)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
        return JWT_AUTH_FLAG + jwt;
    }

    @Deprecated
    static boolean useJwtAuth(HttpServletRequest servletRequest) {
        String auth = getAuth(servletRequest);
        return StringUtils.isNotBlank(auth) && auth.startsWith(JWT_AUTH_FLAG);
    }

    static String getAuth(HttpServletRequest servletRequest) {
        return servletRequest.getHeader(JWT_AUTH_HEADER);
    }


    static JwtPayload parse(String token) {
        Claims claims = io.jsonwebtoken.Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .parseClaimsJws(token.substring(JWT_AUTH_FLAG.length()))
                .getBody();
        return parseClaims(claims);
    }

    public static JwtPayload parseExpired(String token) {
        try {
            JwtUtils.parse(token);
            throw new IllegalStateException("token is not expired");
        }
        catch (ExpiredJwtException e) {
            return parseClaims(e.getClaims());
        }
        catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            throw new AuthenticationException("jwt is illegal", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static JwtPayload parseClaims(Claims claims) {
        return new JwtPayload(claims.getId(),
                claims.getIssuedAt().getTime(),
                claims.get("roles", List.class),
                claims.get("perms", List.class));
    }
}
