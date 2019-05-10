package pub.ron.jwt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pub.ron.jwt.domain.Perm;
import pub.ron.jwt.repository.PermRepository;
import pub.ron.jwt.service.PermService;

/**
 * 权限服务
 *
 * @author ron
 * 2019.05.09
 */
@Service
class DefaultPermService extends BaseServiceAdapter<Perm, PermRepository>
        implements PermService {

    @Autowired
    DefaultPermService(PermRepository repository) {
        super(repository);
    }

}
