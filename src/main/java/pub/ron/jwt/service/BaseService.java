package pub.ron.jwt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pub.ron.jwt.domain.BaseEntity;

import java.util.List;
import java.util.Optional;

/**
 * 顶层服务接口
 * @author ron
 * 2019.05.10
 */
public interface BaseService<T extends BaseEntity> {

    /**
     * 通过id查询
     *
     * @param id id
     * @return 对象
     */
    T findById(Integer id);

    /**
     * 获取所有数据
     *
     * @return 所有数据
     */
    List<T> findAll();

    /**
     * @param pageable 分页对象
     * @return 分页数据
     */
    Page<T> findByPage(Pageable pageable);

    /**
     * 新增对象
     *
     * @param entity 实体
     */
    void add(T entity);

    /**
     * 修改对象
     *
     * @param entity 实体
     */
    void update(T entity);

    /**
     * 通过名字查询
     *
     * @param name 名字
     * @return 实体包装
     */
    Optional<T> findByName(String name);

    /**
     * 检查对象是否存在
     *
     * @param id id
     * @return 是否存在
     */
    boolean exists(Integer id);

    /**
     * 删除对象
     *
     * @param id id
     */
    void delete(Integer id);

}
