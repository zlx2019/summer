package com.zero.summer.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zero.summer.core.enums.OrderState;
import com.zero.summer.core.entity.abstracts.SuperModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单实体
 * @author Zero.
 * @date 2022/6/21 10:18 上午
 */
@EqualsAndHashCode(callSuper = true)
@TableName("sys_order")
@Data
public class Order extends SuperModel<Order> {
    private String title;//订单标题

    private String orderNo;//商户订单编号

    private Long userId;//用户id

    private Long productId;//支付产品id

    private Integer totalFee;//订单金额(分)

    private String codeUrl;//订单二维码连接

    /**
     * 订单状态
     * #{@link OrderState}
     * 0:未支付
     * 1:已生成支付信息,支付中
     * 2:已支付
     * 3:超时已取消
     * 4:用户已取消
     * 5:退款中
     * 6:已退款
     * 7:退款失败
     */
    private OrderState orderStatus;
}
