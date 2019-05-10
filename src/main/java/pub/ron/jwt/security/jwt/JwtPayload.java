package pub.ron.jwt.security.jwt;

import java.util.ArrayList;
import java.util.List;

/**
 * jwt 负载
 * @author ron
 * 2019.01.16
 */
public class JwtPayload {

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


    public JwtPayload(String id,
                      long time,
                      List<String> roles,
                      List<String> perms) {
        this.id = id;
        this.time = time;
        this.roles = new ArrayList<>(roles);
        this.perms = new ArrayList<>(perms);
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
