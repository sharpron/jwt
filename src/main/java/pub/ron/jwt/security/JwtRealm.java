package pub.ron.jwt.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JwtRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRealm.class);


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String token = principals.getPrimaryPrincipal().toString();
        LOGGER.info("doGetAuthorizationInfo" + token);
        JwtPayload payload = JwtTokenizer.parse(token);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(payload.getRoles());
        info.addStringPermissions(payload.getPerms());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        LOGGER.info("doGetAuthenticationInfo" + authenticationToken.getPrincipal());
        String token = authenticationToken.getCredentials().toString();
        JwtTokenizer.checkValid(token);
        setCredentialsMatcher((token1, info) -> true);
        return new SimpleAuthenticationInfo(authenticationToken.getPrincipal(), token, getName());
    }
}
