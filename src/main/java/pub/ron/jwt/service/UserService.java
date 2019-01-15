package pub.ron.jwt.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pub.ron.jwt.domain.RefreshToken;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.dto.Token;
import pub.ron.jwt.repository.UserRepository;
import pub.ron.jwt.security.JwtPayload;
import pub.ron.jwt.security.JwtUtils;
import pub.ron.jwt.security.PasswordUtil;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RefreshTokenService refreshTokenService;

    @Autowired
    public UserService(UserRepository userRepository, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
    }

    public Optional<User> findByUsername(String username) {
        return  userRepository.findUserByUsername(username);
    }


    @Transactional
    public Token sign(String username, String password) {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        SecurityUtils.getSubject().login(usernamePasswordToken);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String jwt = JwtUtils.createToken(user);
        RefreshToken refreshToken = refreshTokenService.createNewerRefreshToken(user);
        return new Token(jwt, refreshToken.getValue());
    }

    public Token reapplyToken(Token oldToken) {
        final RefreshToken refreshToken = refreshTokenService.findByTokenValue(oldToken.getRefreshToken())
                .orElseThrow(() ->
                        new UnknownRefreshTokenException("unknown refresh token " + oldToken.getRefreshToken()));
        if (refreshTokenService.isExpired(refreshToken)) {
            throw new AuthenticationException("refresh token is expired");
        }

        JwtPayload payload = JwtUtils.parseExpired(oldToken.getJwt());
        User user = userRepository.findUserByUsername(payload.getId()).orElseThrow(AssertionError::new);
        if (!user.equals(refreshToken.getUser())) {
            throw new AuthenticationException("refresh token and jwt is not matched");
        }
        String refreshTokenValue = refreshTokenService.updateRefreshToken(refreshToken).getValue();
        String jwt = JwtUtils.createToken(payload.newer());
        return new Token(jwt, refreshTokenValue);
    }


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


    private String handlePassword(String password, String salt) {
        return PasswordUtil.encrypt(password, salt);
    }

    public boolean existUsername(String username) {
        return findByUsername(username).isPresent();
    }



}
