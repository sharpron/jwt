package pub.ron.jwt.service;

import pub.ron.jwt.domain.RefreshToken;
import pub.ron.jwt.domain.User;

/**
 * 刷新token服务
 * @author ron
 * 2019.05.10
 */
public interface RefreshTokenService extends BaseService<RefreshToken> {


    /**
     * 刷新token是否过期
     * @param refreshToken 刷新token
     * @return 过期返回true， 否则返回false
     */
    boolean isExpired(RefreshToken refreshToken);

    /**
     * 更新刷新token
     * @param refreshToken 刷新token
     * @return 刷新token
     */
    RefreshToken updateRefreshToken(RefreshToken refreshToken);

    /**
     * 创建新的刷新token
     * @param user 用户
     * @param isMobile 是否为移动端
     * @return 刷新token
     */
    RefreshToken createNewerRefreshToken(User user, boolean isMobile);
}
