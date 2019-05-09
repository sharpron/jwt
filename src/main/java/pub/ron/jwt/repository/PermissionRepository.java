package pub.ron.jwt.repository;

import org.springframework.stereotype.Repository;
import pub.ron.jwt.domain.Permission;

/**
 * 权限访问
 *
 * @author ron
 * 2019.01.03
 */
@Repository
public interface PermissionRepository extends BaseRepository<Permission> {
}
