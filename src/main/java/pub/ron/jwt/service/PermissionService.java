package pub.ron.jwt.service;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pub.ron.jwt.annotation.PermissionDesc;
import pub.ron.jwt.domain.Permission;
import pub.ron.jwt.repository.PermissionRepository;

/**
 * 权限服务
 * @author ron
 * 2019.05.09
 */
@Service
public class PermissionService extends BaseService<Permission> {

    @Autowired
    public PermissionService(PermissionRepository repository) {
        super(repository);
    }

    /**
     * 通过注解自动生成权限
     * @param desc 权限描述注解
     * @param requiresPermissions shiro权限注解
     */
    public void generatePermissionsByAnnotation(PermissionDesc desc,
                                                RequiresPermissions requiresPermissions,
                                                String uri) {

    }
}
