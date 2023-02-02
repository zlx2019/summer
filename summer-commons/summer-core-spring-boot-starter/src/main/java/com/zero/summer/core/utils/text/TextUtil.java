package com.zero.summer.core.utils.text;

/**
 * 文本处理工具类
 *
 * @author Zero.
 * @date 2023/2/2 8:50 PM
 */
public class TextUtil {
    /**
     * 将首字母转换成小写
     *
     * @param str 需要转换的字符串
     * @return
     */
    public static String toLowerCaseFirstOne(String str) {
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }
}
