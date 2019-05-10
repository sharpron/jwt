package pub.ron.jwt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pub.ron.jwt.domain.Log;
import pub.ron.jwt.repository.LogRepository;
import pub.ron.jwt.service.LogService;

/**
 * 日志服务
 * @author ron
 * 2019.05.08
 */
@Service
class DefaultLogService extends BaseServiceAdapter<Log, LogRepository> implements LogService {

    @Autowired
    DefaultLogService(LogRepository repository) {
        super(repository);
    }
}
