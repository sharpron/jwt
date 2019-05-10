package pub.ron.jwt.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pub.ron.jwt.domain.Log;
import pub.ron.jwt.service.JwtService;
import pub.ron.jwt.service.LogService;
import pub.ron.jwt.util.BrowserUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;

/**
 * 处理日志助手
 * @author ron
 * 2019.05.09
 */
@Component
public class LogHandler extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogHandler.class);

    private static final ThreadLocal<Log> LOG_THREAD_LOCAL = new ThreadLocal<>();

    private final LogService logService;

    @Autowired
    public LogHandler(LogService logService) {
        this.logService = logService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        LOGGER.info("请求地址{}, 参数{}", request.getRequestURI(), request.getParameterMap().toString());
        //静态资源是org.springframework.web.servlet.resource.ResourceHttpRequestHandler
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            LogDesc logDesc = handlerMethod.getMethodAnnotation(LogDesc.class);
            if (logDesc != null) {
                final Log log = new Log();
                log.setName(logDesc.value());
                log.setOperator(JwtService.getAuthenticated().getId());
                log.setCreateTime(Instant.now());
                log.setIp(BrowserUtils.getClientIpAddr(request));
                LOG_THREAD_LOCAL.set(log);
            }
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        final Log log = LOG_THREAD_LOCAL.get();
        if (log != null) {
            LOG_THREAD_LOCAL.remove();
            Duration duration = Duration.between(log.getCreateTime(), Instant.now());
            log.setSpend(duration.toMillis());
            logService.add(log);
        }
    }
}
