package pub.ron.jwt.security.hmac;

import org.apache.tomcat.util.buf.HexUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacUtil {

    private static final String ALG = "HmacMD5";
    private static final byte[] SECRET_KEY = genSecretKey();

    private HmacUtil() {}

    private static byte[] genSecretKey() {
        try {
            KeyGenerator hmacMD5 = KeyGenerator.getInstance(ALG);
            return hmacMD5.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encryptHmac(byte[] data) {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY, ALG);
        try {
            Mac mac = Mac.getInstance(ALG);
            mac.init(secretKey);
            return HexUtils.toHexString(mac.doFinal(data));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
