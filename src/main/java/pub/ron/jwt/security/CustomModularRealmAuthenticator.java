package pub.ron.jwt.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.springframework.stereotype.Component;

@Component
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        assertRealmsConfigured();
        for (Realm realm : getRealms()) {
            if (realm.supports(authenticationToken)) {
                return doSingleRealmAuthentication(realm, authenticationToken);
            }
        }
        return null;
    }
}
