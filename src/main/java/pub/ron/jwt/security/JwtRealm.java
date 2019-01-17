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
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pub.ron.jwt.service.JwtService;

/**
 * jwt 认证和授权的realm
 * @author ron
 * 2019.01.17
 */
@Component
public class JwtRealm extends AuthorizingRealm {


    private final JwtService jwtService;

    @Autowired
    public JwtRealm(JwtService jwtService) {
        this.jwtService = jwtService;
        //jwt由自定义方式验证，不需要密码验证，所以这里默认设置密码匹配通过
        setCredentialsMatcher((token, info) -> true);
    }


    /**
     * 只支持Jwt token
     * @param token jwt
     * @return 能否支持
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 获取授权信息
     * @param principals 认证过的principal
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        JwtPayload payload = (JwtPayload) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(payload.getRoles());
        info.addStringPermissions(payload.getPerms());
        return info;
    }

    /**
     * 获取认证信息
     * @param authenticationToken 认证token
     * @return 认证信息
     * @throws AuthenticationException 认证异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        String token = authenticationToken.getPrincipal().toString();
        try {
            JwtPayload payload = jwtService.parse(token);
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
