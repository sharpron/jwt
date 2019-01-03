package pub.ron.jwt.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.apache.shiro.authc.AuthenticationException;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.List;

public class JwtTokenizer {

    private static final String SECRET = "42340ag0as8gfs8ay2hi24j091fd9f113";

    private static final int ACTIVE_INTERVAL = 60 * 60 * 1000;

    private JwtTokenizer() {
    }

    static void checkValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                    .parseClaimsJws(token);
        }
        catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            throw new AuthenticationException("jwt is not valid", e);
        }
    }

    public static String createToken(JwtPayload payload) {
        return Jwts.builder()
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
    }

    @SuppressWarnings("unchecked")
    static JwtPayload parse(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                .parseClaimsJws(token)
                .getBody();
        return new JwtPayload(claims.getId(),
                claims.getIssuedAt().getTime(),
                claims.get("host", String.class),
                claims.get("roles", List.class),
                claims.get("perms", List.class));
    }
}
