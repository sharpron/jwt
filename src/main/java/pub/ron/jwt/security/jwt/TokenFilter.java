package pub.ron.jwt.security.jwt;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract class TokenFilter extends AccessControlFilter {


    @Override
    protected final boolean isAccessAllowed(ServletRequest servletRequest,
                                      ServletResponse servletResponse, Object o) {
        Subject subject = getSubject(servletRequest, servletResponse);
        return subject != null && subject.isAuthenticated();
    }

    @Override
    protected final boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse)
            throws Exception {
        if (enable(servletRequest)) {
            getSubject(servletRequest, servletResponse)
                    .login(createToken(servletRequest));
            return true;
        }
        return false;
    }

    protected abstract AuthenticationToken createToken(ServletRequest request);

    protected abstract boolean enable(ServletRequest request);

}
