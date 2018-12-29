package pub.ron.jwt.security.hmac;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import pub.ron.jwt.security.jwt.TokenFilter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class HmacFilter extends TokenFilter {

    private static final String DEFAULT_USER_NAME_PARAM = "username";
    private static final String DEFAULT_TIMESTAMP_PARAM = "timestamp";
    private static final String DEFAULT_DIGEST_PARAM = "digest";


    @Override
    protected AuthenticationToken createToken(ServletRequest request) {
        String username = request.getParameter(DEFAULT_USER_NAME_PARAM);
        long timestamp = Long.parseLong(request.getParameter(DEFAULT_TIMESTAMP_PARAM));
        String digest = request.getParameter(DEFAULT_DIGEST_PARAM);
        return new HmacToken(username, digest, timestamp, request.getRemoteHost());
    }

    @Override
    protected boolean enable(ServletRequest request) {
        String username = request.getParameter(DEFAULT_USER_NAME_PARAM);
        String timestamp = request.getParameter(DEFAULT_TIMESTAMP_PARAM);
        String digest = request.getParameter(DEFAULT_DIGEST_PARAM);
        return (request instanceof HttpServletRequest)
                && StringUtils.isNotBlank(username)
                && StringUtils.isNotBlank(timestamp)
                && StringUtils.isNotBlank(digest);
    }
}
