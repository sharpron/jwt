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

    private static final String ALGORITHM_NAME = "md5";
    private static final int ITERATIONS = 512;
    private PasswordUtil() {}

    public static String encrypt(String password, String salt) {
        return new Md5Hash(password, salt, ITERATIONS).toString();
    }

    public static String randomSalt() {
        return UUID.randomUUID().toString();
    }

    static CredentialsMatcher getCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher(ALGORITHM_NAME);
        credentialsMatcher.setHashIterations(ITERATIONS);
        return credentialsMatcher;
    }
}
