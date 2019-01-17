package pub.ron.jwt.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pub.ron.jwt.domain.RefreshToken;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.dto.Token;
import pub.ron.jwt.repository.UserRepository;
import pub.ron.jwt.security.JwtPayload;
import pub.ron.jwt.security.PasswordUtil;

import javax.transaction.Transactional;

/**
 * 用户服务
 * @author ron
 * 2019.01.17
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    private final RefreshTokenService refreshTokenService;

    private final JwtService jwtService;

    @Autowired
    public UserService(UserRepository userRepository,
                       RefreshTokenService refreshTokenService,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    /**
     * 登录认证
     * @param username 用户名
     * @param password 密码
     * @return 认证成功的用户
     */
    @Transactional
    public User login(String username, String password) {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        try {
            SecurityUtils.getSubject().login(usernamePasswordToken);
        }
        catch (UnknownAccountException | IncorrectCredentialsException e) {
            throw new AuthenticationException("用户名或密码错误");
        }
        return (User) SecurityUtils.getSubject().getPrincipal();
    }


    /**
     * 申请token
     * @param user 用户
     * @param isMobile 是否为移动端
     * @return token
     */
    public Token applyToken(User user, boolean isMobile) {
        String jwt = jwtService.createJwt(user, isMobile);
        RefreshToken refreshToken = refreshTokenService.createNewerRefreshToken(user, isMobile);
        return new Token(jwt, refreshToken.getValue());
    }

    /**
     * 使用旧token申请新token
     * @param oldToken 旧token
     * @return 新token
     */
    public Token reapplyToken(Token oldToken) {
        final RefreshToken refreshToken = refreshTokenService.findByTokenValue(oldToken.getRefreshToken())
                .orElseThrow(() ->
                        new UnknownRefreshTokenException("unknown refresh token " + oldToken.getRefreshToken()));
        if (refreshTokenService.isExpired(refreshToken)) {
            throw new AuthenticationException("refresh token is expired");
        }
        JwtPayload payload = jwtService.parseExpired(oldToken.getJwt());
        User user = userRepository.findUserByUsername(payload.getId()).orElseThrow(AssertionError::new);
        if (!user.equals(refreshToken.getUser())) {
            throw new AuthenticationException("refresh token and jwt is not matched");
        }
        String refreshTokenValue = refreshTokenService.updateRefreshToken(refreshToken).getValue();
        String jwt = jwtService.createNewerToken(payload.newer(), refreshToken.isMobile());
        return new Token(jwt, refreshTokenValue);
    }

    /**
     * 用户注册
     * @param user 注册用户
     */
    public void register(User user) {
        if (existUsername(user.getUsername())) {
            throw new RuntimeException("username already existed");
        }
        String salt = PasswordUtil.randomSalt();
        String password = handlePassword(user.getPassword(), salt);
        user.setPassword(password);
        user.setSalt(salt);
        userRepository.save(user);
    }

    /**
     * 处理密码
     * @param password 密码
     * @param salt 盐
     * @return 处理后的密码
     */
    private String handlePassword(String password, String salt) {
        return PasswordUtil.encrypt(password, salt);
    }

    /**
     * 有用户名称了
     * @param username 用户名
     * @return 存在返回true，否则false
     */
    public boolean existUsername(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }


}
