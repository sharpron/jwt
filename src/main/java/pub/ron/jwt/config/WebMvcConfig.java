package pub.ron.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pub.ron.jwt.annotation.LogHandler;

/**
 * 配置相关
 * @author ron
 * 2019.05.09
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LogHandler logHandler;

    @Autowired
    public WebMvcConfig(LogHandler logHandler) {
        this.logHandler = logHandler;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logHandler);
    }
}
