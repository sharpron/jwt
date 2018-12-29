package pub.ron.jwt.security.hmac;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;


@Component
public class HmacRealm extends AuthorizingRealm {

    private final UserRepository userRepository;


    @Autowired
    public HmacRealm(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = principals.getPrimaryPrincipal().toString();
        User user = userRepository.findUserByUsername(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        Set<String> roles = user.getRoles()
                .stream().map(e -> e.getId().toString()).collect(Collectors.toSet());
        simpleAuthorizationInfo.addRoles(roles);
        user.getRoles().forEach(e -> {
            Set<String> permissions = e.getPermissions().stream()
                    .map(permission -> permission.getId().toString()).collect(Collectors.toSet());
            simpleAuthorizationInfo.addStringPermissions(permissions);
        });
        return simpleAuthorizationInfo;
    }

    @Override
    public Class getAuthenticationTokenClass() {
        return HmacToken.class;
    }



    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        HmacToken hmacToken = (HmacToken) token;
        if (hmacToken.expired()) {
            throw new AuthenticationException("数字摘要失效");
        }
        User user = userRepository.findUserByUsername(hmacToken.getPrincipal());
        if (user == null) {
            throw new UnknownAccountException( hmacToken.getPrincipal() + "账户不存在");
        }
        if (user.isDisabled()) {
            throw new LockedAccountException("账户被禁用，请联系管理员");
        }
        return new SimpleAuthenticationInfo(hmacToken.getPrincipal(), Boolean.TRUE, getName());
    }
}
