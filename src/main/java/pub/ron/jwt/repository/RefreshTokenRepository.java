package pub.ron.jwt.repository;

import org.springframework.stereotype.Repository;
import pub.ron.jwt.domain.RefreshToken;

/**
 * 刷新token仓库
 * @author ron
 * 2019.01.03
 */
@Repository
public interface RefreshTokenRepository extends BaseRepository<RefreshToken> {

}
