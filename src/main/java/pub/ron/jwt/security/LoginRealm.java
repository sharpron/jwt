package pub.ron.jwt.security;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.repository.UserRepository;

import java.util.Optional;

public class LoginRealm extends AuthenticatingRealm {

    private final UserRepository userRepository;

    public LoginRealm(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        String username = userToken.getUsername();

        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if(!userOptional.isPresent()){
            throw new UnknownAccountException("用户不存在");
        }
        //5.若用户被锁定，可以抛出LockedAccountException
        if(userOptional.get().isDisabled()){
            throw new DisabledAccountException("用户被禁用");
        }
        ByteSource credentialsSalt = ByteSource.Util.bytes(userOptional.get().getSalt());
        return new SimpleAuthenticationInfo(
                userOptional.get().getUsername(),
                userOptional.get().getPassword(),
                credentialsSalt, getName());

    }
}
