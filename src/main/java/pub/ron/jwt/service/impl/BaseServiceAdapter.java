package pub.ron.jwt.service.impl;


import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pub.ron.jwt.domain.BaseEntity;
import pub.ron.jwt.exception.ExistedAddException;
import pub.ron.jwt.exception.NotExistedUpdateException;
import pub.ron.jwt.exception.ResourceNotFoundException;
import pub.ron.jwt.repository.BaseRepository;
import pub.ron.jwt.service.BaseService;

import java.util.List;
import java.util.Optional;

/**
 * @author ron
 * 2019.05.08
 */
abstract class BaseServiceAdapter<T extends BaseEntity, R extends BaseRepository<T>>
        implements BaseService<T> {

    private  R repository;

    BaseServiceAdapter(R repository) {
        this.repository = repository;
    }

    /**
     * 通过id查询
     *
     * @param id id
     * @return 对象
     */
    @Override
    public  T findById(Integer id) {
        return repository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * 获取所有数据
     *
     * @return 所有数据
     */
    @Override
    public  List<T> findAll() {
        return repository.findAll();
    }

    /**
     * @param pageable 分页对象
     * @return 分页数据
     */
    @Override
    public  Page<T> findByPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * 新增对象
     *
     * @param entity 实体
     */
    @Override
    public  void add(T entity) {
        if (repository.exists(Example.of(entity))) {
            throw new ExistedAddException();
        }
        repository.save(entity);
    }

    /**
     * 修改对象
     *
     * @param entity 实体
     */
    @Override
    public  void update(T entity) {
        if (exists(entity.getId())) {
            repository.save(entity);
        } else {
            throw new NotExistedUpdateException();
        }
    }

    /**
     * 通过名字查询
     *
     * @param name 名字
     * @return 实体包装
     */
    @Override
    public  Optional<T> findByName(String name) {
        return repository.findByName(name);
    }

    /**
     * 检查对象是否存在
     *
     * @param id id
     * @return 是否存在
     */
    @Override
    public  boolean exists(Integer id) {
        return repository.existsById(id);
    }

    /**
     * 删除对象
     *
     * @param id id
     */
    @Override
    public  void delete(Integer id) {
        repository.deleteById(id);
    }

    protected  R getRepository() {
        return repository;
    }
}
