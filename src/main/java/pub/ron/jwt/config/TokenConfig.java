package pub.ron.jwt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * token相关配置
 * @author ron
 * 2019.01.17
 */
@Component
@PropertySource("classpath:/token.properties")
public class TokenConfig {

    private final long webJwtActiveTime;
    private final long webRefreshTokenActiveTime;

    private final long appJwtActiveTime;
    private final long appRefreshTokenActiveTime;

    public TokenConfig(@Value("${web.jwt.active-time}") long webJwtActiveTime,
                       @Value("${web.refresh-token.active-time}") long webRefreshTokenActiveTime,
                       @Value("${app.jwt.active-time}") long appJwtActiveTime,
                       @Value("${app.refresh-token.active-time}") long appRefreshTokenActiveTime) {
        this.webJwtActiveTime = webJwtActiveTime;
        this.webRefreshTokenActiveTime = webRefreshTokenActiveTime;
        this.appJwtActiveTime = appJwtActiveTime;
        this.appRefreshTokenActiveTime = appRefreshTokenActiveTime;
    }

    /**
     * @return web端jwt有效期
     */
    public long getWebJwtActiveTime() {
        return webJwtActiveTime;
    }

    /**
     *
     * @return web端刷新token 有效期
     */
    public long getWebRefreshTokenActiveTime() {
        return webRefreshTokenActiveTime;
    }

    /**
     *
     * @return app端jwt有效期
     */
    public long getAppJwtActiveTime() {
        return appJwtActiveTime;
    }

    /**
     *
     * @return app刷新token的有效期
     */
    public long getAppRefreshTokenActiveTime() {
        return appRefreshTokenActiveTime;
    }
}
