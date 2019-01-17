package pub.ron.jwt.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.springframework.stereotype.Component;

/**
 * 重写 {@link ModularRealmAuthenticator} 使得异常处理正常
 * 多realm的情况下，realm单独处理
 * @author ron
 * 2019.01.17
 */
@Component
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        assertRealmsConfigured();
        for (Realm realm : getRealms()) {
            if (realm.supports(authenticationToken)) {
                return doSingleRealmAuthentication(realm, authenticationToken);
            }
        }
        return null;
    }
}
