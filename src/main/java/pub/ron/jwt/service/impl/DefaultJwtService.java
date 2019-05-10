package pub.ron.jwt.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pub.ron.jwt.config.TokenConfig;
import pub.ron.jwt.domain.Perm;
import pub.ron.jwt.domain.Role;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.security.jwt.JwtPayload;
import pub.ron.jwt.service.JwtService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * json web token 服务
 * 以下简称jwt
 *
 * @author ron
 * 2019.05.09
 */
@Service
class DefaultJwtService implements JwtService {

    private static final String SECRET = "42340ag0as8gfs8ay2hi24j091fd9f113";

    private static final String JWT_AUTH_FLAG = "Bearer ";
    private static final String CLAIMS_ROLES = "roles";
    private static final String CLAIMS_PERMS = "perms";

    private final TokenConfig tokenConfig;

    @Autowired
    DefaultJwtService(TokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
    }

    /**
     * 根据用户创建jwt
     *
     * @param user     用户
     * @param isMobile 是否是移动端
     * @return jwt
     */
    @Override
    public String createJwt(User user, boolean isMobile) {
        JwtPayload payload = new JwtPayload(user.getName(),
                System.currentTimeMillis(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toList()),
                user.getRoles().stream().flatMap(e -> e.getPerms().stream())
                        .map(Perm::getName).collect(Collectors.toList()));
        return createJwt(payload, isMobile);
    }

    /**
     * 根据负载创建jwt
     *
     * @param payload  负载
     * @param isMobile 是否是移动端
     * @return jwt
     */
    private String createJwt(JwtPayload payload, boolean isMobile) {
        long activeTime = isMobile ? tokenConfig.getAppJwtActiveTime()
                : tokenConfig.getWebJwtActiveTime();
        String jwt = Jwts.builder()
                .setId(payload.getId())
                .setIssuedAt(new Date(payload.getTime()))
                .setExpiration(new Date(payload.getTime() + activeTime))
                .claim(CLAIMS_ROLES, payload.getRoles())
                .claim(CLAIMS_PERMS, payload.getPerms())
                .compressWith(CompressionCodecs.GZIP)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
        return JWT_AUTH_FLAG + jwt;
    }

    /**
     * 根据已有的（旧的）负载生成新的jwt
     *
     * @param oldPayload 旧的负载
     * @param isMobile   是否是移动端
     * @return 新的jwt
     */
    @Override
    public String createNewerJwt(JwtPayload oldPayload, boolean isMobile) {
        final JwtPayload newerPayload = new JwtPayload(oldPayload.getId(),
                System.currentTimeMillis(), oldPayload.getRoles(),
                oldPayload.getPerms());
        return createJwt(newerPayload, isMobile);
    }

    /**
     * 解析jwt
     *
     * @param jwt jwt
     * @return 负载
     */
    @Override
    public JwtPayload parseJwt(String jwt) throws IllegalJwtException, ExpiredJwtException {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                    .parseClaimsJws(jwt.substring(JWT_AUTH_FLAG.length()));
            return valueOf(claimsJws.getBody());
        } catch (UnsupportedJwtException | MalformedJwtException
                | SignatureException | IllegalArgumentException | DecodingException e) {
            throw new IllegalJwtException(String.format("非法jwt：{%s}，请检查正确性", jwt));
        }
    }

    @Override
    public JwtPayload parseExpiredJwt(String jwt) {
        try {
            parseJwt(jwt);
            throw new IllegalStateException();
        } catch (ExpiredJwtException e) {
            return valueOf(e.getClaims());
        }
    }

    /**
     * 将申明转为负载
     *
     * @param claims 声明
     * @return 负载
     */
    @SuppressWarnings("unchecked")
    private JwtPayload valueOf(Claims claims) {
        return new JwtPayload(claims.getId(),
                claims.getIssuedAt().getTime(),
                claims.get(CLAIMS_ROLES, List.class),
                claims.get(CLAIMS_PERMS, List.class)
        );
    }
}
