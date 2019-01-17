package pub.ron.jwt.security;

import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import pub.ron.jwt.domain.Permission;
import pub.ron.jwt.domain.Role;
import pub.ron.jwt.domain.User;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * jwt 负载
 * @author ron
 * 2019.01.16
 */
public class JwtPayload {

    private static final String CLAIMS_ROLES = "roles";

    private static final String CLAIMS_PERMS = "perms";

    private static final int ACTIVE_INTERVAL =  60 * 1000;

    /**
     * id , username, 或者其它，唯一标志
     */
    private final String id;
    /**
     * 生成时间
     */
    private final long time;

    /**
     * 拥有的角色
     */
    private final List<String> roles;

    /**
     * 拥有的权限
     */
    private final List<String> perms;


    public JwtPayload(String id, long time,
                      List<String> roles, List<String> perms) {
        this.id = id;
        this.time = time;
        this.roles = roles;
        this.perms = perms;
    }

    public String getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<String> getPerms() {
        return perms;
    }

    /**
     * @return 生成当前时间的新备份
     */
    public JwtPayload newer() {
        return new JwtPayload(this.getId(), System.currentTimeMillis(), roles, perms);
    }

    @Override
    public String toString() {
        return "JwtPayload{" +
                "id='" + id + '\'' +
                ", time=" + time +
                ", roles=" + roles +
                ", perms=" + perms +
                '}';
    }

    /**
     * @return claims
     */
    public Map<String, Object> toClaims() {
        final Map<String, Object> map = Maps.newHashMap();
        map.put(Claims.ID, getId());
        map.put(Claims.ISSUED_AT, new Date(time));
        map.put(Claims.EXPIRATION, new Date(time + ACTIVE_INTERVAL));
        map.put(CLAIMS_ROLES, roles);
        map.put(CLAIMS_PERMS, perms);
        return map;
    }

    /**
     * @return 获取认证成功的
     */
    public static JwtPayload getAuthenticated() {
        return (JwtPayload) SecurityUtils.getSubject().getPrincipal();
    }


    /**
     * @param claims jwt 申明
     * @return 负载
     */
    @SuppressWarnings("unchecked")
    public static JwtPayload valueOf(Claims claims) {
        return new JwtPayload(claims.getId(),
                claims.getIssuedAt().getTime(),
                claims.get(CLAIMS_ROLES, List.class),
                claims.get(CLAIMS_PERMS, List.class));
    }
}
