package com.zero.summer.core.utils.request;

import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.HttpResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 获取地址工具类
 *
 * @author Zero.
 * @date 2023/3/2 11:50 AM
 */
@Slf4j
public class AddressUtils {
    private AddressUtils(){}

    // IP地址查询
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";
    // 位置地址
    private static final String UNKNOWN = "UNKNOWN";

    /**
     * 根据IP 获取对应的地区名字
     * @param ip    要查询的ip
     * @return      省份/城市
     */
    public static String getRealAddressInfo(String ip)  {
        if (!IPUtil.isIP(ip)){
            return UNKNOWN;
        }
        // 局域网ip
        if (IPUtil.internalIp(ip)){
            return "0.0.0.0";
        }
        // 以json格式响应
        HttpResult result = HttpUtil.getSync(IP_URL, Map.of("ip", ip,"json",true));
        if (!result.isSuccessful()){
            return UNKNOWN;
        }
        // 解析Json
        JSONObject object = JSONObject.parseObject(result.getBody().toString());
        // 获取省份
        String pro = object.getString("pro");
        // 获取城市
        String city = object.getString("city");
        return pro;
    }
}
