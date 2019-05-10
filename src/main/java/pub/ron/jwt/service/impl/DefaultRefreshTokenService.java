package pub.ron.jwt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import pub.ron.jwt.config.TokenConfig;
import pub.ron.jwt.domain.RefreshToken;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.repository.RefreshTokenRepository;
import pub.ron.jwt.service.RefreshTokenService;

import java.time.Duration;
import java.time.Instant;

/**
 * 刷新token处理
 *
 * @author ron
 * 2019.01.17
 */
@Service
class DefaultRefreshTokenService extends BaseServiceAdapter<RefreshToken, RefreshTokenRepository>
        implements RefreshTokenService {

    private final TokenConfig tokenConfig;

    private final RefreshTokenRepository refreshTokenRepository;


    @Autowired
    DefaultRefreshTokenService(TokenConfig tokenConfig,
                               RefreshTokenRepository refreshTokenRepository) {
        super(refreshTokenRepository);
        this.tokenConfig = tokenConfig;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * 刷新token是否过期
     *
     * @param refreshToken 刷新token
     * @return 过期返回true， 否则返回false
     */
    @Override
    public boolean isExpired(RefreshToken refreshToken) {
        Duration duration = Duration.between(refreshToken.getUpdateTime(), Instant.now());
        if (refreshToken.isMobile()) {
            return duration.toMillis() > tokenConfig.getAppRefreshTokenActiveTime();
        }
        return duration.toMillis() > tokenConfig.getWebRefreshTokenActiveTime();
    }

    /**
     * 更新刷新token
     *
     * @param refreshToken 刷新token
     * @return 刷新token
     */
    @Override
    public RefreshToken updateRefreshToken(RefreshToken refreshToken) {
        refreshToken.setUpdateTime(Instant.now());
        refreshToken.setName(genToken(refreshToken.getUser().getPassword()));
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    /**
     * 创建新的刷新token
     *
     * @param user     用户
     * @param isMobile 是否为移动端
     * @return 刷新token
     */
    @Override
    public RefreshToken createNewerRefreshToken(User user, boolean isMobile) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setMobile(isMobile);
        refreshToken.setUser(user);
        return updateRefreshToken(refreshToken);
    }

    /**
     * 生成token
     *
     * @param source 源
     * @return token值
     */
    private static String genToken(String source) {
        return DigestUtils.md5DigestAsHex((source + System.currentTimeMillis()).getBytes());
    }
}
