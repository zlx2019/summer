package com.zero.summer.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态枚举
 *
 * @author Zero.
 * @date 2022/6/22 11:51 上午
 */
@Getter
@AllArgsConstructor
public enum OrderState {
    NOT_PAY(0,"未支付"),
    PAYMENTS(1,"已生成第三方支付信息,支付中"),
    SUCCESS(2,"已支付"),
    CLOSE(3,"超时已取消"),
    CANCEL(4,"用户已取消"),
    REFUND_PROCESSING(5,"退款中"),
    REFUND_SUCCESS(6,"已退款"),
    REFUND_ABNORMAL(7,"退款失败");
    ;

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String value;

    /**
     * 断言两个状态枚举是否一致
     * @param state 要判断的状态枚举
     */
    public boolean Is(OrderState state){
        if (state.equals(this)){
            return true;
        }
        return false;
    }
}
