package pub.ron.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pub.ron.jwt.domain.Log;
import pub.ron.jwt.repository.LogRepository;
import pub.ron.jwt.security.jwt.JwtPayload;

/**
 * 日志服务
 * @author ron
 * 2019.05.08
 */
@Service
public class LogService extends BaseService<Log> {

    @Autowired
    public LogService(LogRepository jpaRepository) {
        super(jpaRepository);
    }


    /**
     * 创建日志，不保存到数据库
     * @param logMsg 日志消息
     * @return 日志
     */
    public Log createLog(String logMsg) {
        Log log = new Log();
        log.setName(logMsg);
        JwtPayload authenticated = JwtPayload.getAuthenticated();
        log.setOperator(authenticated.getId());
        return log;
    }
}
