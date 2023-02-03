package com.zero.summer.db.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zero.summer.core.holder.UserContextHolder;
import com.zero.summer.core.utils.snowflakes.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * MP 属性填充实现
 *
 * @author Zero.
 * @date 2022/1/19 11:36 上午
 */
@Slf4j
public class ModelAutoFillHandler implements MetaObjectHandler {

    /**
     * 自动填充五位字段
     * id:         ID
     * createTime: 创建时间
     * createUser: 创建者ID
     * updateTime: 最后一次修改时间
     * updateUser: 最后一次修改者ID
     */
    private final static String CREATE_TIME = "createTime";
    private final static String UPDATE_TIME = "updateTime";
    private final static String CREATE_USER = "createUser";
    private final static String UPDATE_USER = "updateUser";
    private final static String ID = "id";

    /**
     * 新增事件填充
     * @param metaObject 元实体
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //填充主键ID
        setFieldValByName(ID, IdGenerator.getId(),metaObject);
        //填充创建时间
        this.strictInsertFill(metaObject,CREATE_TIME,LocalDateTime::now,LocalDateTime.class);
        //填充更新时间
        this.strictInsertFill(metaObject,UPDATE_TIME,LocalDateTime::now,LocalDateTime.class);
        //填充创建者与更新者
        Long userId = UserContextHolder.getUser();
        if (Objects.nonNull(userId)){
            this.strictInsertFill(metaObject,CREATE_USER,()-> userId,Long.class);
            this.strictInsertFill(metaObject,UPDATE_USER,()-> userId,Long.class);
        }
    }

    /**
     * 更新时间填充
     * @param metaObject 元实体
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        //填充更新时间
        this.strictUpdateFill(metaObject,UPDATE_TIME,LocalDateTime::now,LocalDateTime.class);
        //填充更新者
        Long userId = UserContextHolder.getUser();
        if (Objects.nonNull(userId)){
            this.strictUpdateFill(metaObject,UPDATE_USER,()-> userId,Long.class);
        }
    }
}
