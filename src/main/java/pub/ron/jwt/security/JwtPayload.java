package pub.ron.jwt.security;

import java.util.List;

public class JwtPayload {

    private final String id;

    private final long time;

    private final String host;

    private final List<String> roles;

    private final List<String> perms;

    public JwtPayload(String id, long time, String host,
                      List<String> roles, List<String> perms) {
        this.id = id;
        this.time = time;
        this.host = host;
        this.roles = roles;
        this.perms = perms;
    }

    public String getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public String getHost() {
        return host;
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<String> getPerms() {
        return perms;
    }
}
