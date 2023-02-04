package com.summer.core.utils.text;

import cn.hutool.core.util.StrUtil;

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

    public static void main(String[] args) {
        Map<String,Object> param = new HashMap<>();
        param.put("username","张三");
        param.put("age",null);
        String format = TextUtil.format("i name is {username},age is {age}", param);
        System.out.println(format);
    }
}
