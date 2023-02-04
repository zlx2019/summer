package com.zero.summer.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.summer.core.constant.Constant;
import com.zero.summer.core.lock.DistributedLock;
import com.zero.summer.core.utils.valid.AssertUtil;

import java.util.Optional;

/**
 * MP通用Service
 * 定义一些通用数据访问层API,进行扩展
 *
 * @author Zero.
 * @date 2022/1/23 1:56 下午
 */
public interface ISuperService<T> extends IService<T> {

    /**
     * 幂等性新增数据
     *
     * @param model            实体对象
     * @param lock             分布式锁
     * @param lockKey          锁的key
     * @param conditionWrapper 是否已存在的条件Wrapper
     * @param message          提示信息
     * @return 操作是否成功
     */
    boolean constantSave(T model, DistributedLock lock, String lockKey, Wrapper<T> conditionWrapper, String message);


    /**
     * 幂等性新增或者修改数据
     *
     * @param model            实体对象
     * @param lock             分布式锁
     * @param lockKey          锁的Key
     * @param conditionWrapper 是否已存在的条件Wrapper
     * @param message          提示信息
     * @return 操作是否成功
     */
    boolean constantSaveOrModify(T model, DistributedLock lock, String lockKey, Wrapper<T> conditionWrapper, String message);


    /**
     * 通用查询 -- 根据主键查询
     * 根据主键 获取对应的实体,通过{@link Optional}包装后返回
     *
     * @param id    主键
     * @return      Optional<T>
     */
    default Optional<T> queryEntityById(Long id){
        return Optional.ofNullable(getById(id));
    }

    /**
     * 通用查询 -- 分页+条件查询
     *
     * @param curPage   页码
     * @param pageSize  页容量
     * @param wrapper   条件
     * @return          分页响应结果对象
     */
    default Page<T> listPage(Integer curPage, Integer pageSize, Wrapper<T> wrapper){
        curPage = Optional.ofNullable(curPage).orElse(Constant.DEFAULT_PAGE);
        pageSize = Optional.ofNullable(pageSize).orElse(Constant.DEFAULT_PAGE_SIZE);
        AssertUtil.isMin(curPage,Constant.DEFAULT_PAGE,Constant.PAGE_MIN_MESSAGE);
        return this.page(new Page<>(curPage, pageSize), wrapper);
    }
    default Page<T> listPage(Integer curPage, Integer pageSize){
        return listPage(curPage,pageSize,null);
    }

}
