package pub.ron.jwt.annotation;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限处理助手
 *
 * @author ron
 * 2019.05.09
 */
@Component
public class PermHandler extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            PermDefine permDefine = handlerMethod.getMethodAnnotation(PermDefine.class);
            checkPerm(permDefine);
        }
        return super.preHandle(request, response, handler);
    }

    /**
     * 检查权限, 如果不存在将抛出{@link UnauthorizedException}异常
     *
     * @param permDefine 权限定义
     */
    private static void checkPerm(PermDefine permDefine) {
        if (permDefine != null && !isPermitted(permDefine.name())) {
            throw new UnauthorizedException(String.format("用户不具有[%s:%s]权限",
                    permDefine.desc(), permDefine.name()));
        }
    }

    /**
     * 是否有该权限
     *
     * @param permName 权限名称
     * @return 是否有该权限
     */
    private static boolean isPermitted(String permName) {
        return SecurityUtils.getSubject().isPermitted(permName);
    }

}
