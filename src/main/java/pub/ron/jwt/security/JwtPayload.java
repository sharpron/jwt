package pub.ron.jwt.security;

import org.apache.shiro.SecurityUtils;
import pub.ron.jwt.domain.Permission;
import pub.ron.jwt.domain.Role;
import pub.ron.jwt.domain.User;

import java.util.List;
import java.util.stream.Collectors;

public class JwtPayload {

    private final String id;

    private final long time;

    private final List<String> roles;

    private final List<String> perms;

    JwtPayload(User user) {
        this(String.valueOf(user.getUsername()), System.currentTimeMillis(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toList()),
                user.getRoles().stream().flatMap(e -> e.getPermissions().stream()).map(Permission::getName).collect(Collectors.toList()));
    }

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

    public static JwtPayload getAuthenticated() {
        return (JwtPayload) SecurityUtils.getSubject().getPrincipal();
    }

    public JwtPayload newer() {
        return new JwtPayload(this.getId(), System.currentTimeMillis(),
                getRoles(), getPerms());
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
}
