package pub.ron.jwt.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pub.ron.jwt.config.TokenConfig;
import pub.ron.jwt.domain.Permission;
import pub.ron.jwt.domain.Role;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.security.jwt.JwtPayload;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtService {

    private static final String SECRET = "42340ag0as8gfs8ay2hi24j091fd9f113";

    private static final String JWT_AUTH_FLAG = "Bearer ";

    private final TokenConfig tokenConfig;

    @Autowired
    private JwtService(TokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
    }

    String createJwt(User user, boolean isMobile) {
        JwtPayload payload = new JwtPayload(user.getUsername(), System.currentTimeMillis(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toList()),
                user.getRoles().stream().flatMap(e -> e.getPermissions().stream())
                        .map(Permission::getName).collect(Collectors.toList()));
        return createToken(payload, isMobile);
    }


    private String createToken(JwtPayload payload, boolean isMobile)
        throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {
        long activeTime = isMobile ? tokenConfig.getAppJwtActiveTime()
                : tokenConfig.getWebJwtActiveTime();
        String jwt = Jwts.builder()
                .setId(payload.getId())
                .setIssuedAt(new Date(payload.getTime()))
                .setExpiration(new Date(payload.getTime() + activeTime))
                .claim("roles", payload.getRoles())
                .claim("perms", payload.getPerms())
                .compressWith(CompressionCodecs.GZIP)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
        return JWT_AUTH_FLAG + jwt;
    }

    String createNewerToken(JwtPayload oldPayload, boolean isMobile) {
        return createToken(oldPayload.newer(), isMobile);
    }

    public JwtPayload parse(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .parseClaimsJws(token.substring(JWT_AUTH_FLAG.length()))
                .getBody();
        return JwtPayload.valueOf(claims);
    }

    JwtPayload parseExpired(String token) {
        try {
            parse(token);
            throw new IllegalStateException("token is not expired");
        }
        catch (ExpiredJwtException e) {
            return JwtPayload.valueOf(e.getClaims());
        }
        catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            throw new AuthenticationException("jwt is illegal", e);
        }
    }

}
