package pub.ron.jwt.service;


import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pub.ron.jwt.domain.BaseEntity;
import pub.ron.jwt.exception.ExistedAddException;
import pub.ron.jwt.exception.NotExistedUpdateException;
import pub.ron.jwt.exception.ResourceNotFoundException;
import pub.ron.jwt.repository.BaseRepository;

import java.util.Optional;

/**
 * @author ron
 * 2019.05.08
 */
public abstract class BaseService<T extends BaseEntity> {

    private final BaseRepository<T> repository;

    BaseService(BaseRepository<T> jpaRepository) {
        this.repository = jpaRepository;
    }

    /**
     * 通过id查询
     * @param id id
     * @return 对象
     */
    public T findById(Integer id) {
        return repository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     *
     * @param pageable 分页对象
     * @return 分页数据
     */
    public Page<T> findByPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * 新增对象
     * @param entity 实体
     */
    public void add(T entity) {
        if (repository.exists(Example.of(entity))) {
            throw new ExistedAddException();
        }
        repository.save(entity);
    }

    /**
     * 修改对象
     * @param entity 实体
     */
    public void update(T entity) {
        if (exists(entity.getId())) {
            repository.save(entity);
        }
        else {
            throw new NotExistedUpdateException();
        }
    }

    /**
     * 通过名字查询
     * @param name 名字
     * @return 实体包装
     */
    public Optional<T> findByName(String name) {
        return repository.findByName(name);
    }

    /**
     * 检查对象是否存在
     * @param id id
     * @return 是否存在
     */
    public boolean exists(Integer id) {
        return repository.existsById(id);
    }

    /**
     * 删除对象
     * @param id id
     */
    public void delete(Integer id) {
        repository.deleteById(id);
    }

}
