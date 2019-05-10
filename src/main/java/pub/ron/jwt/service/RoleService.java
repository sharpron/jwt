package pub.ron.jwt.service;

import pub.ron.jwt.domain.Role;

/**
 * 角色服务
 * @author ron
 * 2019.05.10
 */
public interface RoleService extends BaseService<Role> {

    /**
     * 如果不是系统角色才删除
     */
    void deleteIfNotSystem(Integer id);
}
