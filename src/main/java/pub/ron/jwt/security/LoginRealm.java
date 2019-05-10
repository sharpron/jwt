package pub.ron.jwt.security;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.repository.UserRepository;

import java.util.Optional;

/**
 * 登录处理
 * @author ron
 * 2019.01.17
 */
@Component
public class LoginRealm extends AuthenticatingRealm {

    /**
     * 用户仓库
     */
    private final UserRepository userRepository;

    /**
     * 处理指定token
     * @param token {@link UsernamePasswordToken}
     * @return 支持返回true，否则返回false
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    /**
     * 构造器注入
     * @param userRepository 用户仓库
     */
    @Autowired
    public LoginRealm(UserRepository userRepository) {
        this.userRepository = userRepository;
        //设置密码匹配器
        setCredentialsMatcher(PasswordUtil.getCredentialsMatcher());
    }

    /**
     * 获取认证信息
     * @param token 认证token
     * @return 认证信息
     * @throws AuthenticationException 认证出现相关问题将抛出该异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {

        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        String username = userToken.getUsername();

        Optional<User> userOptional = userRepository.findByName(username);
        if(!userOptional.isPresent()){
            throw new UnknownAccountException("用户不存在");
        }
        if(userOptional.get().isDisabled()){
            throw new DisabledAccountException("用户被禁用");
        }
        ByteSource credentialsSalt = ByteSource.Util.bytes(userOptional.get().getSalt());
        return new SimpleAuthenticationInfo(
                userOptional.get(),
                userOptional.get().getPassword(),
                credentialsSalt, getName());

    }
}
