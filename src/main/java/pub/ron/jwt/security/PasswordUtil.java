package pub.ron.jwt.security;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.UUID;

/**
 * @author ron
 * 2019.01.03
 */
public class PasswordUtil {
    /**
     * 算法名称
     */
    private static final String ALGORITHM_NAME = "md5";
    /**
     * 处理次数
     */
    private static final int ITERATIONS = 512;

    /**
     * 工具类禁用构造器
     */
    private PasswordUtil() {}

    /**
     * 使用指定算法处理密码
     * @param password 密码
     * @param salt 盐
     * @return 处理好的密码
     */
    public static String encrypt(String password, String salt) {
        return new Md5Hash(password, salt, ITERATIONS).toString();
    }

    /**
     * 随机盐生成
     * @return 随机盐
     */
    public static String randomSalt() {
        return UUID.randomUUID().toString();
    }

    /**
     * 使用指定的算法创建的密码匹配器，用以验证密码相同
     * @return 密码匹配器
     */
    static CredentialsMatcher getCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher(ALGORITHM_NAME);
        credentialsMatcher.setHashIterations(ITERATIONS);
        return credentialsMatcher;
    }
}
