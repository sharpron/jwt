package pub.ron.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pub.ron.jwt.domain.Permission;
import pub.ron.jwt.domain.User;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
