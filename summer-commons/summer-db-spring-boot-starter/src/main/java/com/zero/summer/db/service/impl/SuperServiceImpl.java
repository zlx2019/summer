package com.zero.summer.db.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.summer.core.enums.ExceptionMsgEnum;
import com.zero.summer.core.exception.BusinessException;
import com.zero.summer.core.lock.DistributedLock;
import com.zero.summer.core.lock.LockResult;
import com.zero.summer.db.service.ISuperService;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * {@link ISuperService}实现类,继承自MybatisPlus的{@link ServiceImpl}
 * Service 通用实现层
 * 实现通用扩展API
 *
 * @author Zero.
 * @date 2022/4/1 9:10 上午
 */
@Slf4j
public class SuperServiceImpl<M extends BaseMapper<T>,T> extends ServiceImpl<M,T> implements ISuperService<T> {


    /**
     * 幂等性新增数据
     *
     * 例子如下：
     * String username = sysUser.getUsername();
     * boolean result = super.saveIdempotency(sysUser, lock
     *                 , LOCK_KEY_USERNAME+username
     *                 , new QueryWrapper<SysUser>().eq("username", username));
     *
     * @param model     实体对象
     * @param lock      分布式锁
     * @param lockKey   锁的key
     * @param conditionWrapper 是否已存在的条件Wrapper
     * @param message   提示信息
     * @return
     */
    @Override
    public boolean constantSave(T model, DistributedLock lock, String lockKey, Wrapper<T> conditionWrapper, String message) {
        Optional.ofNullable(lock).orElseThrow(()-> new BusinessException(ExceptionMsgEnum.LOCK_NULL.getMessage()));
        Optional.ofNullable(lockKey).orElseThrow(()-> new BusinessException(ExceptionMsgEnum.LOCK_KEY_NULL.getMessage()));
        try {
            //加锁
            LockResult lockResult = lock.lock(lockKey);
            if (lockResult.isLock()){
                //是否已存在记录,
                if (super.count(conditionWrapper) == 0){
                    return super.save(model);
                }else {
                    //已存在
                    return false;
                }
            }else {
                //加锁超时
                throw new BusinessException(ExceptionMsgEnum.LOCK_TIME_OUT.getMessage());
            }
        }finally {
            //必须释放锁
            lock.releaseLock(lockKey);
        }
    }

    /**
     *  幂等性新增或者修改数据
     *  例子如下：
     * String username = sysUser.getUsername();
     * boolean result = super.saveOrUpdateIdempotency(sysUser, lock
     *                 , LOCK_KEY_USERNAME+username
     *                 , new QueryWrapper<SysUser>().eq("username", username));
     *
     * @param model     实体对象
     * @param lock      分布式锁
     * @param lockKey   锁的Key
     * @param conditionWrapper  是否已存在的条件Wrapper
     * @param message   提示信息
     * @return
     */
    @Override
    public boolean constantSaveOrModify(T model, DistributedLock lock, String lockKey, Wrapper<T> conditionWrapper, String message) {
        if (model == null) return false;
        Class<?> cls = model.getClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
        if (null == tableInfo || StringUtils.isBlank(tableInfo.getKeyProperty())){
            throw new BusinessException("Error:  Can not execute. Could not find @TableId.");
        }
        Object idVal = ReflectionKit.getFieldValue(model, tableInfo.getKeyProperty());
        if (StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal))) {
            if (StrUtil.isEmpty(message)) message = "数据已存在";
            return this.constantSave(model, lock, lockKey, conditionWrapper, message);
        }else {
            return updateById(model);
        }
    }
}
