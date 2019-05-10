package pub.ron.jwt.repository;

import org.springframework.stereotype.Repository;
import pub.ron.jwt.domain.Role;

/**
 * 角色仓库
 * @author ron
 * 2019.01.03
 */
@Repository
public interface RoleRepository extends BaseRepository<Role> {
}
