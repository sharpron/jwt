package pub.ron.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pub.ron.jwt.domain.RefreshToken;
import pub.ron.jwt.domain.Role;
import pub.ron.jwt.domain.User;

import java.util.Optional;

/**
 * 刷新token仓库
 * @author ron
 * 2019.01.03
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByValue(String value);

}
