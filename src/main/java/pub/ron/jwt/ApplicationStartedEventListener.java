package pub.ron.jwt;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import pub.ron.jwt.annotation.PermissionDesc;
import pub.ron.jwt.domain.Permission;
import pub.ron.jwt.service.PermissionService;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 应用启动完成后执行
 * @author ron
 * 2019.05.09
 */
@Component
public class ApplicationStartedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartedEventListener.class);

    private final PermissionService permissionService;

    @Autowired
    public ApplicationStartedEventListener(PermissionService permissionService) {
        this.permissionService = permissionService;
    }


    private void savePermission(PermissionDesc desc,
                                       RequiresPermissions permissions,
                                       Set<RequestMethod> methods,
                                       Set<String> patterns) {
        if (desc != null && permissions != null) {
            String[] perms = permissions.value();
            if (perms.length > 1) {
                throw new RuntimeException("暂时只支持声明一个权限");
            }
            String permName = perms[0];
            Optional<Permission> byName = permissionService.findByName(permName);
            Permission permission = byName.orElse(new Permission());
            permission.setName(permName);
            permission.setDesc(desc.value());
            permission.setMethods(methods);
            permission.setUriPatterns(patterns);
            if (byName.isPresent()) {
                permissionService.update(permission);
            }
            else {
                permissionService.add(permission);
            }

        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Set<String> result = new HashSet<>();
        WebApplicationContext wc = (WebApplicationContext) applicationContext;
        RequestMappingHandlerMapping bean = wc.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            PermissionDesc permissionDesc = handlerMethod.getMethodAnnotation(PermissionDesc.class);
            RequiresPermissions requiresPermissions = handlerMethod.getMethodAnnotation(RequiresPermissions.class);
            Set<String> patterns = entry.getKey().getPatternsCondition().getPatterns();
            Set<RequestMethod> methods = entry.getKey().getMethodsCondition().getMethods();
            LOGGER.debug(patterns.toString());
            LOGGER.debug(methods.toString());
            if (permissionDesc != null && requiresPermissions != null) {
                savePermission(permissionDesc,
                        requiresPermissions, methods, patterns);
            }
        }
        for (RequestMappingInfo rmi : handlerMethods.keySet()) {
            PatternsRequestCondition pc = rmi.getPatternsCondition();
            Set<String> pSet = pc.getPatterns();
            result.addAll(pSet);
        }
    }
}