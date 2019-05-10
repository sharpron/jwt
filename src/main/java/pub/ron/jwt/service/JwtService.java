package pub.ron.jwt.service;

import io.jsonwebtoken.ExpiredJwtException;
import org.apache.shiro.SecurityUtils;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.security.jwt.JwtPayload;
import pub.ron.jwt.service.impl.IllegalJwtException;

/**
 * json web token 服务
 * @author ron
 * 2019.05.10
 */
public interface JwtService {

    /**
     * 根据用户创建jwt
     * @param user 用户
     * @param isMobile 是否是移动端
     * @return jwt
     */
    String createJwt(User user, boolean isMobile);

    /**
     * 根据已有的（旧的）负载生成新的jwt
     * @param oldPayload 旧的负载
     * @param isMobile 是否是移动端
     * @return 新的jwt
     */
    String createNewerJwt(JwtPayload oldPayload, boolean isMobile);

    /**
     * 解析jwt
     * @param jwt jwt
     * @throws IllegalJwtException jwt是非法的将抛出该异常
     * @throws ExpiredJwtException jwt过期异常
     * @return 负载
     */
    JwtPayload parseJwt(String jwt) throws IllegalJwtException, ExpiredJwtException;

    /**
     * 解析过期的jwt
     * @param jwt 过期的jwt
     * @throws IllegalJwtException jwt是非法的将抛出该异常
     * @return 负载
     */
    JwtPayload parseExpiredJwt(String jwt) throws IllegalJwtException;

    /**
     * @return 获取认证成功的
     */
    static JwtPayload getAuthenticated() {
        return (JwtPayload) SecurityUtils.getSubject().getPrincipal();
    }

}
