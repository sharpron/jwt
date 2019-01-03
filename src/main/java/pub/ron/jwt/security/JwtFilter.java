package pub.ron.jwt.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt处理过滤器
 * @author ron
 * 2019.01.03
 */
public class JwtFilter extends AccessControlFilter {

    /**
     * 添加跨域访问支持
     * @param request 请求
     * @param response 响应
     * @return 继续处理的标记，详见{@link AccessControlFilter#preHandle(ServletRequest, ServletResponse)}
     * @throws Exception 详见父类
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                      Object mappedValue) throws Exception {
        boolean authenticated = getSubject(request, response).isAuthenticated();
        return authenticated;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        if (JwtTokenizer.useJwtAuth(httpServletRequest)) {
            JwtPayload payload;
            try {
                payload = JwtTokenizer.parse(JwtTokenizer.getAuth(httpServletRequest));
            }
            catch (ExpiredJwtException e) {
                payload = JwtTokenizer.parseClaims(e.getClaims());
                if (JwtTokenizer.couldRefresh(payload)) {
                    payload = payload.newer();
                    String token = JwtTokenizer.createToken(payload);
                    JwtTokenizer.addAuthToResponseHeader(WebUtils.toHttp(response), token);
                }
                else {
                    throw new AuthenticationException("jwt is expired", e);
                }
            }
            catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
                throw new AuthenticationException("jwt is illegal", e);
            }
            getSubject(request, response).login(new JwtToken(payload));
            return true;
        }
        return false;
    }
}
