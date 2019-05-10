package pub.ron.jwt.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限描述，使用该注解替代RequiresPermissions
 * @see org.apache.shiro.authz.annotation.RequiresPermissions
 * @author ron
 * 2019.05.08
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermDefine {

    /**
     * @return 权限名称
     */
    String name();

    /**
     * @return 描述内容
     */
    String desc();
}
