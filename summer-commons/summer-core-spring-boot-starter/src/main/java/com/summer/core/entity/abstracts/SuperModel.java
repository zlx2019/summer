package com.summer.core.entity.abstracts;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.summer.core.constant.Constant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 通用数据访问层实体基类
 * 该类继承于MP的通用实体 {@link Model},进行定制化扩展。
 * 所有的数据访问层实体都应继承于此对象
 *
 * @author Zero.
 * @date 2023/2/4 6:12 PM
 */
@Getter
@Setter
public abstract class SuperModel<T extends Model<?>> extends Model<T> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID,每张表的主键统一命名为`id`
     * INPUT: insert前自动填充主键值
     * 序列化为json时 转成字符串
     */
    @TableId(type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = Constant.DATETIME_FORMAT, timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = Constant.DATETIME_FORMAT, timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 是否已经逻辑删除
     * false = 0 = 未删除
     * true = 1 = 已删除
     */
    @TableLogic
    private Boolean deleted;
}
