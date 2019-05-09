package pub.ron.jwt.repository;

import org.springframework.stereotype.Repository;
import pub.ron.jwt.domain.Log;

/**
 * 日志操作
 * @author ron
 * 2019.05.08
 */
@Repository
public interface LogRepository extends BaseRepository<Log> {
}
