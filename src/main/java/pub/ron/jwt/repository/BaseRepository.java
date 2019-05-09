package pub.ron.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pub.ron.jwt.domain.BaseEntity;

import java.util.Optional;

/**
 * @author ron
 * 2019.05.09
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Integer> {

    /**
     * @param name 名字
     * @return 包装对象
     */
    Optional<T> findByName(String name);
}
