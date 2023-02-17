package com.zero.summer.core.utils;


import com.zero.summer.core.utils.valid.AssertUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 金额工具类
 *
 * @author Zero.
 * @date 2022/7/5 8:38 PM
 */
public class MoneyUtil {

    /**
     * 元转分（乘以100）
     * @param price 金额:元
     * @return      金额:分
     */
    public static BigDecimal changeY2F(BigDecimal price){
        AssertUtil.isTrue(price.compareTo(BigDecimal.valueOf(0.01)) < 0,"金额不能低于0.01元");
        return price.multiply(new BigDecimal(100)).setScale(2);
    }
    public static BigDecimal changeY2F(Double price){
        return changeY2F(BigDecimal.valueOf(price));
    }

    /**
     * 分转元（除以100）四舍五入，保留2位小数
     * @param price 金额:分
     * @return      金额:元
     */
    public static BigDecimal changeF2Y(BigDecimal price){
        AssertUtil.isTrue(price.compareTo(BigDecimal.ONE) < 0,"金额不能低于1分钱！");
        return price.divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
    }
    public static BigDecimal changeF2Y(Integer price){
        return changeF2Y(BigDecimal.valueOf(price));
    }
    public static BigDecimal changeF2Y(String price){
        return changeF2Y(new BigDecimal(price));
    }


    public static void main(String[] args) {
        System.out.println(MoneyUtil.changeY2F(BigDecimal.valueOf(0.00)));
    }
}
