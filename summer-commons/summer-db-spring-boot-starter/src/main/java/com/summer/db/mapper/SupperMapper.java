package com.summer.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 公共通用Mapper 所有的DAO实体Mapper均继承与此,可在这扩展公共通用抽象方法。
 * @author Zero.
 * @date 2022/1/19 10:53 上午
 */
public interface SupperMapper<T> extends BaseMapper<T> {

}
