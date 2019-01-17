package pub.ron.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import pub.ron.jwt.config.TokenConfig;
import pub.ron.jwt.domain.RefreshToken;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.repository.RefreshTokenRepository;

import java.util.Date;
import java.util.Optional;

/**
 * 刷新token处理
 * @author ron
 * 2019.01.17
 */
@Component
public class RefreshTokenService {

    private final TokenConfig tokenConfig;


    private final RefreshTokenRepository refreshTokenRepository;


    @Autowired
    public RefreshTokenService(TokenConfig tokenConfig, RefreshTokenRepository refreshTokenRepository) {
        this.tokenConfig = tokenConfig;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * 通过token查找刷新token
     * @param tokenValue token值
     * @return RefreshToken
     */
    Optional<RefreshToken> findByTokenValue(String tokenValue) {
        return refreshTokenRepository.findByValue(tokenValue);
    }

    /**
     * 刷新token是否过期
     * @param refreshToken 刷新token
     * @return 过期返回true， 否则返回false
     */
    boolean isExpired(RefreshToken refreshToken) {
        long timeDiff = System.currentTimeMillis() - refreshToken.getTime().getTime();
        if (refreshToken.isMobile()) {
            return timeDiff > tokenConfig.getAppRefreshTokenActiveTime();
        }
        return timeDiff > tokenConfig.getWebRefreshTokenActiveTime();
    }

    /**
     * 更新刷新token
     * @param refreshToken 刷新token
     * @return 刷新token
     */
    RefreshToken updateRefreshToken(RefreshToken refreshToken) {
        refreshToken.setTime(new Date());
        refreshToken.setValue(genToken(refreshToken.getUser().getPassword()));
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    /**
     * 创建新的刷新token
     * @param user 用户
     * @param isMobile 是否为移动端
     * @return 刷新token
     */
    RefreshToken createNewerRefreshToken(User user, boolean isMobile) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setMobile(isMobile);
        refreshToken.setUser(user);
        return updateRefreshToken(refreshToken);
    }

    /**
     * 生成token
     * @param source 源
     * @return token值
     */
    private static String genToken(String source) {
        return DigestUtils.md5DigestAsHex((source + System.currentTimeMillis()).getBytes());
    }
}
