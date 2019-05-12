package pub.ron.jwt.service.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pub.ron.jwt.domain.RefreshToken;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.dto.Token;
import pub.ron.jwt.repository.UserRepository;
import pub.ron.jwt.security.PasswordUtil;
import pub.ron.jwt.security.jwt.JwtPayload;
import pub.ron.jwt.service.JwtService;
import pub.ron.jwt.service.RefreshTokenService;
import pub.ron.jwt.service.UserService;

import javax.transaction.Transactional;

/**
 * 用户服务
 *
 * @author ron
 * 2019.01.17
 */
@Service
class DefaultUserService extends BaseServiceAdapter<User, UserRepository> implements UserService {

    private final RefreshTokenService refreshTokenService;

    private final JwtService jwtService;

    @Autowired
    DefaultUserService(UserRepository userRepository,
                       RefreshTokenService refreshTokenService,
                       JwtService jwtService) {
        super(userRepository);
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public User login(String username, String password) {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        try {
            SecurityUtils.getSubject().login(usernamePasswordToken);
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            throw new AuthenticationException("用户名或密码错误");
        }
        return (User) SecurityUtils.getSubject().getPrincipal();
    }


    @Override
    public Token applyToken(User user, boolean isMobile) {
        String jwt = jwtService.createJwt(user, isMobile);
        RefreshToken refreshToken = refreshTokenService.createNewerRefreshToken(user, isMobile);
        return new Token(jwt, refreshToken.getName());
    }

    @Override
    public Token reapplyToken(Token oldToken) {
        final RefreshToken refreshToken = refreshTokenService.findByName(oldToken.getRefreshToken())
                .orElseThrow(() ->
                        new UnknownRefreshTokenException("unknown refresh token " + oldToken.getRefreshToken()));
        if (refreshTokenService.isExpired(refreshToken)) {
            throw new AuthenticationException("refresh token is expired");
        }
        JwtPayload payload = jwtService.parseJwt(oldToken.getJwt());
        User user = getRepository().findByName(payload.getId()).orElseThrow(AssertionError::new);
        if (!user.equals(refreshToken.getUser())) {
            throw new AuthenticationException("refresh token and jwt is not matched");
        }
        String refreshTokenValue = refreshTokenService.updateRefreshToken(refreshToken).getName();
        String jwt = jwtService.createNewerJwt(payload, refreshToken.isMobile());
        return new Token(jwt, refreshTokenValue);
    }

    @Override
    public void register(User user) {
        if (existUsername(user.getName())) {
            throw new RuntimeException("username already existed");
        }
        String salt = PasswordUtil.randomSalt();
        String password = PasswordUtil.encrypt(user.getPassword(), salt);
        user.setPassword(password);
        user.setSalt(salt);
        getRepository().save(user);
    }

    @Override
    public void modifyPassword(User user) {
        if (user.getId() != null && StringUtils.hasText(user.getPassword())) {

        }
    }

    /**
     * 有用户名称了
     *
     * @param username 用户名
     * @return 存在返回true，否则false
     */
    private boolean existUsername(String username) {
        return getRepository().findByName(username).isPresent();
    }


}
