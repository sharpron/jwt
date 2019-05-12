package pub.ron.jwt.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限描述，使用该注解替代
 * {@link org.apache.shiro.authz.annotation.RequiresPermissions} 等相关权限
 * 使用在{@link org.springframework.stereotype.Controller} 的类型上的时候，该权限
 * 作为菜单展示，如果使用在方法上，定义为方法级别的权限
 * @author ron
 * 2019.05.08
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
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
