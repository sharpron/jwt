package pub.ron.jwt.repository;

import org.springframework.stereotype.Repository;
import pub.ron.jwt.domain.User;

/**
 * 用户仓库
 * @author ron
 * 2019.01.03
 */
@Repository
public interface UserRepository extends BaseRepository<User> {
}
