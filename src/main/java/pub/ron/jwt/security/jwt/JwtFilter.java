package pub.ron.jwt.security.jwt;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class JwtFilter extends TokenFilter {

    private static final String JWT_HEADER = "Authorization";

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);



    @Override
    protected AuthenticationToken createToken(ServletRequest request) {
        String jwtFromHeader = getJwtFromHeader(request);
        LOGGER.debug(jwtFromHeader);
        return new JwtToken(jwtFromHeader, request.getRemoteHost());
    }

    @Override
    protected boolean enable(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            return StringUtils.isNotBlank(getJwtFromHeader(request));
        }
        return false;
    }

    private static String getJwtFromHeader(ServletRequest request) {
        return ((HttpServletRequest) request).getHeader(JWT_HEADER);
    }
}
