package pub.ron.jwt.security.jwt;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.NestedServletException;
import pub.ron.jwt.dto.Error;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt处理过滤器
 * @author ron
 * 2019.01.03
 */
public class JwtFilter extends AdviceFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 转换为格式化的json
        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        if (handleCrossOrigin(httpServletRequest, httpServletResponse)) {
            return authenticate(httpServletRequest, httpServletResponse);
        }
        return false;
    }

    /**
     * 处理跨域
     * @param request 请求
     * @param response 响应
     * @return 是否继续执行
     */
    private static boolean handleCrossOrigin(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            response.setStatus(HttpStatus.OK.value());
            return false;
        }
        return true;
    }

    /**
     * 使用jwt进行认证
     * @param request 请求
     * @param response 响应
     * @return 认证通过为true， 否则为false
     * @throws IOException 可能抛出IO异常
     */
    private static boolean authenticate(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        try {
            SecurityUtils.getSubject()
                    .login(new JwtToken(JwtUtils.getAuth(request)));
            return true;
        }
        catch (AuthenticationException e) {
            outputUnAuthorizeMessage(response, e.getMessage());
            return false;
        }
    }

    @Override
    public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            super.doFilterInternal(request, response, chain);
        }
        catch (NestedServletException e) {
            if (e.getCause() instanceof UnauthorizedException) {
                String message = e.getCause().getMessage();
                LOGGER.warn(message);
                outputUnAuthorizeMessage(WebUtils.toHttp(response), message);
            }
            else {
                LOGGER.error(e.getMessage(), e);
                throw e;
            }
        }
    }

    /**
     * 输出未授权的消息到流
     * @param response 响应
     * @param message 消息
     * @throws IOException io异常
     */
    private static void outputUnAuthorizeMessage(HttpServletResponse response, String message)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        OBJECT_MAPPER.writeValue(response.getOutputStream(), Error.from(message));
    }
}
