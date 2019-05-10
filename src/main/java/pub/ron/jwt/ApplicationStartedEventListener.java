package pub.ron.jwt;

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
import pub.ron.jwt.annotation.PermDefine;
import pub.ron.jwt.domain.Perm;
import pub.ron.jwt.service.PermService;

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

    private final PermService permService;

    @Autowired
    public ApplicationStartedEventListener(PermService permService) {
        this.permService = permService;
    }


    private void savePermission(PermDefine permDefine,
                                Set<RequestMethod> methods,
                                Set<String> patterns) {
        if (permDefine != null) {
            String name = permDefine.name();
            Optional<Perm> byName = permService.findByName(name);
            Perm perm = byName.orElse(new Perm());
            perm.setName(name);
            perm.setDescription(permDefine.desc());
            perm.setMethods(methods);
            perm.setUriPatterns(patterns);
            if (byName.isPresent()) {
                permService.update(perm);
            }
            else {
                permService.add(perm);
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
            PermDefine permissionDesc = handlerMethod.getMethodAnnotation(PermDefine.class);
            Set<String> patterns = entry.getKey().getPatternsCondition().getPatterns();
            Set<RequestMethod> methods = entry.getKey().getMethodsCondition().getMethods();
            LOGGER.debug(patterns.toString());
            LOGGER.debug(methods.toString());
            if (permissionDesc != null) {
                savePermission(permissionDesc, methods, patterns);
            }
        }
        for (RequestMappingInfo rmi : handlerMethods.keySet()) {
            PatternsRequestCondition pc = rmi.getPatternsCondition();
            Set<String> pSet = pc.getPatterns();
            result.addAll(pSet);
        }
    }
}