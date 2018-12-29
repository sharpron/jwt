package pub.ron.jwt.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pub.ron.jwt.repository.UserRepository;

import javax.xml.bind.DatatypeConverter;
import java.util.Set;


@Component
public class JwtRealm extends AuthorizingRealm {



    private final UserRepository userRepository;


    @Autowired
    public JwtRealm(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Claims claims = (Claims) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(claims.get("roles", Set.class));
        info.setStringPermissions(claims.get("permissions", Set.class));
        return info;
    }

    @Override
    public Class getAuthenticationTokenClass() {
        return JwtToken.class;
    }



    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) token;
        String jwtContent = jwtToken.getPrincipal().toString();
        Claims body = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(""))
                .parseClaimsJws(jwtContent)
                .getBody();
        return new SimpleAuthenticationInfo(body, Boolean.TRUE, getName());
    }
}
