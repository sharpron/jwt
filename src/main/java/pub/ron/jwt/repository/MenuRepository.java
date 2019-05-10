package pub.ron.jwt.repository;

import org.springframework.stereotype.Repository;
import pub.ron.jwt.domain.Menu;

import java.util.List;

/**
 * @author ron
 * 2019.05.09
 */
@Repository
public interface MenuRepository extends BaseRepository<Menu> {

    /**
     * 获取顶层菜单
     * @return 顶层菜单
     */
    List<Menu> findMenusByParentIsNull();
}
