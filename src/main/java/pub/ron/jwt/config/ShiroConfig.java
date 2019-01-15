package pub.ron.jwt.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import pub.ron.jwt.security.CustomModularRealmAuthenticator;
import pub.ron.jwt.security.JwtFilter;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * shiro 配置
 * @author ron
 * 2019.01.03
 */
@Configuration
public class ShiroConfig {

    private static final String JWT_FILTER = "jwt";
    private static final String ANON = "anon";


    /**
     * @param realms 认证域
     * @return 安全管理器
     */
    @Bean
    public DefaultWebSecurityManager securityManager(List<Realm> realms, CustomModularRealmAuthenticator authenticator) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealms(realms);
        authenticator.setRealms(realms);
        securityManager.setAuthenticator(authenticator);
        DefaultSubjectDAO subjectDAO = (DefaultSubjectDAO) securityManager.getSubjectDAO();
        // 关闭自带session
        DefaultSessionStorageEvaluator evaluator = (DefaultSessionStorageEvaluator) subjectDAO.getSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(evaluator);
        return securityManager;
    }

    /**
     * 配置shiro的过滤链
     * @param securityManager 安全管理器
     * @return ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean factory(SecurityManager securityManager) {
        SecurityUtils.setSecurityManager(securityManager);
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put(JWT_FILTER, new JwtFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        Map<String, String> filterRuleMap = new HashMap<>();
        //登陆相关api不需要被过滤器拦截

        filterRuleMap.put("/**", JWT_FILTER);
        filterRuleMap.put("/authentication/*", ANON);
        filterRuleMap.put("/user/sign", ANON);
        filterRuleMap.put("/swagger-ui.html", ANON);
        filterRuleMap.put("/swagger-resources", ANON);
        filterRuleMap.put("/v2/api-docs", ANON);
        filterRuleMap.put("/webjars/**", ANON);
        filterRuleMap.put("/swagger-resources/**", ANON);
        filterRuleMap.put("/csrf", ANON);
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);

        return factoryBean;
    }

    /**
     * 添加注解依赖
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    /**
     * 添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true); // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 开启注解验证
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
