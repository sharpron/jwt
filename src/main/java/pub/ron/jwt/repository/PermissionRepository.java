package pub.ron.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import pub.ron.jwt.domain.Permission;
import pub.ron.jwt.domain.User;

/**
 * 权限访问
 * @author ron
 * 2019.01.03
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
