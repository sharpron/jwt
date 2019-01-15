package pub.ron.jwt.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;
import pub.ron.jwt.service.UserService;

@Component
public class RefreshTokenRealm extends AuthenticatingRealm {

    private UserService userService;

    public RefreshTokenRealm() {
        setCredentialsMatcher((token, info) -> true);
    }


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof RefreshAuthcToken;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        String token = authenticationToken.getPrincipal().toString();
        try {
            JwtPayload payload = JwtUtils.parse(token);
            return new SimpleAuthenticationInfo(payload, payload, getName());
        }
        catch (ExpiredJwtException e) {
            throw new AuthenticationException("jwt已经过期", e);
        }
        catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            throw new AuthenticationException("jwt is illegal", e);
        }
    }
}
