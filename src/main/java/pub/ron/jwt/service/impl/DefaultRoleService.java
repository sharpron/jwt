package pub.ron.jwt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pub.ron.jwt.domain.Role;
import pub.ron.jwt.repository.RoleRepository;
import pub.ron.jwt.service.RoleService;

/**
 * 角色服务
 * @author ron
 * 2019.05.09
 */
@Service
class DefaultRoleService extends BaseServiceAdapter<Role, RoleRepository> implements RoleService {


    @Autowired
    DefaultRoleService(RoleRepository repository) {
        super(repository);
    }


    @Override
    public void deleteIfNotSystem(Integer id) {
        Role role = findById(id);
        if (role.isSystem()) {
            throw new RuntimeException("系统角色，不能够删除");
        }
        super.delete(id);
    }
}
