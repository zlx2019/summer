package com.zero.summer.core.utils.text;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 文本处理工具类
 *
 * @author Zero.
 * @date 2023/2/2 8:50 PM
 */
public class TextUtil {
    private TextUtil(){

    }

    /**
     * 将字符串首字母转换成小写
     *
     * @param str 需要转换的字符串
     * @return    转换后的字符串
     */
    public static String toLowerCaseFirstOne(String str) {
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }


    /**
     * 字符串格式化
     *
     * @param template 字符串模板
     * @param params   参数
     * @return         格式化后的字符串
     *
     * 占位符统一使用 {}表示
     * 举栗
     * ("i name is {}, age is {}","张三",18) => "i name is 张三, age is 18"
     */
    public static String format(CharSequence template,Object... params){
        return StrUtil.format(template,params);
    }

    /**
     * 字符串格式化
     *
     * @param template 字符串模板
     * @param params   参数
     * @return         格式化后的字符串
     *
     * 参数以Map形式传入,占位符以{key}表示,格式化将{key}转换为对应的value,如果值为Null,则替换为""
     *
     * 举栗
     * Map:{"username":"张三","age": Null}
     * ("i name is {username},age is {age}",Map) => i name is 张三,age is
     */
    public static String format(CharSequence template, Map<String,Object> params){
        return StrUtil.format(template,params,false);
    }


    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为星号<例子：李**>
     *
     * @param fullName 姓名
     * @return 脱敏后的名字
     */
    public static String chineseName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return "";
        }
        String result = null;
        char[] chars = fullName.toCharArray();
        if (chars.length == 1) {
            result = fullName;
        }
        if (chars.length == 2) {
            result = fullName.replaceFirst(fullName.substring(1), "*");
        }
        if (chars.length > 2) {
            result = fullName.replaceAll(fullName.substring(1, chars.length - 1), "*");
        }
        return result;
    }

    /**
     * [中文姓名] 只显示姓氏，其他隐藏为星号<例子：欧阳娜娜  ： 欧阳**>
     *
     * @param familyName 姓氏
     * @param givenName  名字
     * @return 脱敏后的名字
     */
    public static String chineseName(String familyName, String givenName) {
        if (StringUtils.isBlank(familyName) || StringUtils.isBlank(givenName)) {
            return "";
        }
        if (familyName.length() > 1) {
            String name = StringUtils.left(familyName, familyName.length());
            return StringUtils.rightPad(name, StringUtils.length(familyName + givenName), "*");
        }
        return chineseName(familyName + givenName);
    }

    /**
     * [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     *
     * @param id 身份证号
     * @return 脱敏结果
     */
    public static String idCardNum(String id) {
        if (StringUtils.isBlank(id)) {
            return "";
        }
        String num = StringUtils.right(id, 4);
        return StringUtils.leftPad(num, StringUtils.length(id), "*");
    }

    /**
     * [身份证号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:451002********1647>
     *
     * @param carId 身份证号
     * @return 脱敏结果
     */
    public static String idCard(String carId) {
        if (StringUtils.isBlank(carId)) {
            return "";
        }
        return StringUtils.left(carId, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(carId, 4), StringUtils.length(carId), "*"), "******"));
    }

    /**
     * [固定电话] 后四位，其他隐藏<例子：****1234>
     *
     * @param num 固定电话
     * @return 脱敏结果
     */
    public static String fixedPhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*");
    }

    /**
     * [手机号码] 前三位，后四位，其他隐藏<例子:138******1234>
     *
     * @param num 手机号码
     * @return 脱敏结果
     */
    public static String mobilePhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.left(num, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*"), "***"));
    }

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     *
     * @param address       地址
     * @param sensitiveSize 敏感信息长度
     * @return 脱敏结果
     */
    public static String address(String address, int sensitiveSize) {
        if (StringUtils.isBlank(address)) {
            return "";
        }
        int length = StringUtils.length(address);
        return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, "*");
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     *
     * @param email 电子邮箱
     * @return 脱敏结果
     */
    public static String email(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        int index = StringUtils.indexOf(email, "@");
        if (index <= 1) {
            return email;
        } else {
            return StringUtils.rightPad(StringUtils.left(email, 1), index, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
        }
    }

    /**
     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234>
     *
     * @param cardNum 银行卡号
     * @return 脱敏结果
     */
    public static String bankCard(String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        return StringUtils.left(cardNum, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"), "******"));
    }

    /**
     * [公司开户银行联号] 公司开户银行联行号,显示前两位，其他用星号隐藏，每位1个星号<例子:12********>
     *
     * @param code 公司开户银行联号
     * @return 脱敏结果
     */
    public static String cnapsCode(String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        return StringUtils.rightPad(StringUtils.left(code, 2), StringUtils.length(code), "*");
    }

    public static void main(String[] args) {
        Map<String,Object> param = new HashMap<>();
        param.put("username","张三");
        param.put("age",null);
        String format = TextUtil.format("i name is {username},age is {age}", param);
        System.out.println(format);
    }
}
