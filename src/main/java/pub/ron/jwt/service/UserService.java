package pub.ron.jwt.service;


import pub.ron.jwt.domain.User;
import pub.ron.jwt.dto.Token;

/**
 * 用户服务
 *
 * @author ron
 * 2019.05.10
 */
public interface UserService extends BaseService<User> {

    /**
     * 登录认证
     *
     * @param username 用户名
     * @param password 密码
     * @return 认证成功的用户
     */
    User login(String username, String password);


    /**
     * 申请token
     *
     * @param user     用户
     * @param isMobile 是否为移动端
     * @return token
     */
    Token applyToken(User user, boolean isMobile);

    /**
     * 使用旧token申请新token
     *
     * @param oldToken 旧token
     * @return 新token
     */
    Token reapplyToken(Token oldToken);

    /**
     * 用户注册
     *
     * @param user 注册用户
     */
    void register(User user);

    /**
     * 修改用户密码
     *
     * @param user 用户
     */
    void modifyPassword(User user);


}
