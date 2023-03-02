package com.zero.summer.core.utils.request;

import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.OkHttps;
import com.ejlchina.okhttps.OnCallback;
import com.ejlchina.okhttps.jackson.JacksonMsgConvertor;
import com.zero.summer.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Http请求 工具类 基于Ok_https
 *
 * @author Zero.
 * @date 2021/10/29 9:00 上午
 */
@Slf4j
public class HttpUtil {

    //客户端
    private static final HTTP CLIENT;

    //初始化客户端
    static {
        CLIENT = HTTP.builder()
                //.baseUrl("http://localhost:12001")
                .bodyType(OkHttps.JSON)
                .charset(StandardCharsets.UTF_8)
                .addMsgConvertor(new JacksonMsgConvertor())
                .exceptionListener((httpTask, e) -> {
                    log.error("Http请求出现异常:", e);
                    throw new BusinessException("【HTTP请求异常】");
                })
                .build();
    }

    /**==================================Get================================================*/

    /**
     * 发送同步Get请求 结果序列化为对应实体
     *
     * @param url    请求Url
     * @param params Query参数
     * @param clazz  序列化结果
     */
    public static <T> T getSyncToBean(String url, Map<String, Object> params, Class<T> clazz) {
        return getSync(url, params, null).getBody().toBean(clazz);
    }

    /**
     * 发送Get请求(同步) 结果序列化为 T
     *
     * @return T
     */
    public static <T> T getSyncToBean(String url, Map<String, Object> params, Map<String, String> headers, Class<T> clazz) {
        return getSync(url, params, headers).getBody().toBean(clazz);
    }

    /**
     * 发送同步Get请求 结果序列化为List
     *
     * @param url    请求Url
     * @param params Query参数
     * @return 结果
     */
    public static <T> List<T> getSyncToList(String url, Map<String, Object> params, Class<T> clazz) {
        return getSync(url, params, null).getBody().toList(clazz);
    }

    /**
     * 发送同步Get请求 结果序列化为List<T>
     *
     * @return List<T>
     */
    public static <T> List<T> getSyncToList(String url, Map<String, Object> params, Map<String, String> headers, Class<T> clazz) {
        return getSync(url, params, headers).getBody().toList(clazz);
    }

    /**
     * 发送Get 同步请求
     *
     * @param url     请求Url
     * @param params  Query参数
     * @param headers 请求头
     * @return 请求结果
     */
    public static HttpResult getSync(String url, Map<String, Object> params, Map<String, String> headers) {
        return CLIENT.sync(url).addUrlPara(params).addHeader(headers).get();
    }
    /**
     * 发送Get 同步请求
     *
     * @param url     请求Url
     * @param params  Query参数
     * @return 请求结果
     */
    public static HttpResult getSync(String url, Map<String, Object> params) {
        return CLIENT.sync(url).addUrlPara(params).get();
    }

    /**
     * 发送Get 异步请求
     *
     * @param url      请求Url
     * @param callback 回调函数
     * @param query    Query参数
     * @param headers  请求头
     */
    public static void getAsync(String url, OnCallback<HttpResult> callback, Map<String, String> query, Map<String, String> headers) {
        CLIENT.async(url).addUrlPara(query).addHeader(headers)
                .setOnResponse(callback).get();
    }

    /**
     * 发送Get 异步请求
     *
     * @param url      请求Url
     * @param callback 回调函数
     * @param query    Query参数
     */
    public static void getAsync(String url, OnCallback<HttpResult> callback, Map<String, String> query) {
        getAsync(url, callback, query, null);
    }

    /**
     * 发送Get 异步请求
     *
     * @param url      请求Url
     * @param callback 回调函数
     */
    public static void getAsync(String url, OnCallback<HttpResult> callback) {
        getAsync(url, callback, null);
    }

    /**
     * 发送Get 请求(rest风格)
     *
     * @param url     请求url
     * @param urlPar  path参数
     * @param headers 请求头
     * @return 请求结果
     */
    public static HttpResult getSyncForRest(String url, Map<String, Object> urlPar, Map<String, String> headers) {
        return CLIENT.sync(url)
                .addPathPara(urlPar)
                .addHeader(headers)
                .get();
    }

    /**
     * 发送Get 同步请求(restful风格) 结果序列化为T
     *
     * @param url     请求Url
     * @param urlPar  path参数
     * @param headers 请求头
     * @return T
     */
    public static <T> T getSyncForRestToBean(String url, Map<String, Object> urlPar, Map<String, String> headers, Class<T> clazz) {
        return getSyncForRest(url, urlPar, headers).getBody().toBean(clazz);
    }

    /**
     * 发送Get 同步请求(restful风格) 结果序列化为T
     *
     * @param url    请求Url
     * @param urlPar path参数
     * @return T
     */
    public static <T> T getSyncForRestToBean(String url, Map<String, Object> urlPar, Class<T> clazz) {
        return getSyncForRestToBean(url, urlPar, null, clazz);
    }

    /**
     * 发送Get 同步请求(restful风格) 结果序列化为T
     *
     * @param url 请求Url
     * @return T
     */
    public static <T> T getSyncForRestToBean(String url, Class<T> clazz) {
        return getSyncForRestToBean(url, null, clazz);
    }

    /**
     * 发送Get 同步请求(restful风格) 结果序列化为List<T>
     *
     * @param url     请求Url
     * @param urlPar  path参数
     * @param headers 请求头
     * @return List<T>
     */
    public static <T> List<T> getSyncForRestToList(String url, Map<String, Object> urlPar, Map<String, String> headers, Class<T> clazz) {
        return getSyncForRest(url, urlPar, headers).getBody().toList(clazz);
    }

    /**
     * 发送Get 同步请求(restful风格) 结果序列化为List<T>
     *
     * @param url    请求Url
     * @param urlPar path参数
     * @return List<T>
     */
    public static <T> List<T> getSyncForRestToList(String url, Map<String, Object> urlPar, Class<T> clazz) {
        return getSyncForRestToList(url, urlPar, null, clazz);
    }

/**==================================Post================================================*/

    /**
     * 发送post 同步请求
     *
     * @param url     请求url
     * @param body    请求体
     * @param headers 请求头
     * @return 请求结果
     */
    public static HttpResult postSync(String url, Map<String, Object> body, Map<String, String> headers) {
        return CLIENT.sync(url)
                .addBodyPara(body)
                .addHeader(headers)
                .post();
    }

    /**
     * 发送post 同步请求 结果序列化为T
     *
     * @param url     请求url
     * @param body    请求体
     * @param headers 请求头
     * @return T
     */
    public static <T> T postSyncToBean(String url, Map<String, Object> body, Map<String, String> headers, Class<T> clazz) {
        return postSync(url, body, headers).getBody().toBean(clazz);
    }

    /**
     * 发送post 同步请求 结果序列化为T
     *
     * @param url  请求url
     * @param body 请求体
     * @return T
     */
    public static <T> T postSyncToBean(String url, Map<String, Object> body, Class<T> clazz) {
        return postSyncToBean(url, body, null, clazz);
    }

    /**
     * 发送post 同步请求 结果序列化为T
     *
     * @param url 请求url
     * @return T
     */
    public static <T> T postSyncToBean(String url, Class<T> clazz) {
        return postSyncToBean(url, null, clazz);
    }

    /**
     * 发送post 同步请求 结果序列化为List
     *
     * @param url     请求url
     * @param body    请求体
     * @param headers 请求头
     * @return List<T>
     */
    public static <T> List<T> postSyncToList(String url, Map<String, Object> body, Map<String, String> headers, Class<T> clazz) {
        return postSync(url, body, headers).getBody().toList(clazz);
    }

    /**
     * 发送Post 同步请求  结果序列化为List
     *
     * @param url  请求Url
     * @param body 请求体
     * @return List<T>
     */
    public static <T> List<T> postSyncToList(String url, Map<String, Object> body, Class<T> clazz) {
        return postSyncToList(url, body, null, clazz);
    }

    /**
     * 发送Post 同步请求  结果序列化为List
     *
     * @param url 请求Url
     * @return List<T>
     */
    public static <T> List<T> postSyncToList(String url, Class<T> clazz) {
        return postSyncToList(url, null, clazz);
    }

    /**
     * 发送post 异步请求
     *
     * @param url      请求url
     * @param callback 回调函数
     * @param body     请求体
     * @param headers  请求头
     * @return 响应结果
     */
    public static void postAsync(String url, OnCallback<HttpResult> callback, Map<String, Object> body, Map<String, String> headers) {
        CLIENT.async(url).addBodyPara(body).addHeader(headers).setOnResponse(callback).post();
    }

    /**
     * 发送Post 异步请求
     *
     * @param url      请求Url
     * @param callback 回调函数
     * @param body     请求体
     */
    public static void postAsync(String url, OnCallback<HttpResult> callback, Map<String, Object> body) {
        postAsync(url, callback, body, null);
    }

    /**
     * 发送Post 异步请求
     *
     * @param url      请求Url
     * @param callback 回调函数
     */
    public static void postAsync(String url, OnCallback<HttpResult> callback) {
        postAsync(url, callback, null);
    }


    /**==================================Put================================================*/

    /**
     * 发送Put 同步请求
     *
     * @param url       请求Url
     * @param body      请求体
     * @param headers   请求头
     * @return          请求结果
     */
    public static HttpResult putSync(String url,Map<String, Object> body,Map<String, String> headers){
        return CLIENT.sync(url).addHeader(headers).addBodyPara(body).put();
    }

    /**
     * 发送Put 同步请求 结果序列化为 T
     *
     * @param url       请求Url
     * @param body      请求体
     * @param headers   请求头
     * @return          T
     */
    public static <T> T putSyncToBean(String url,Map<String, Object> body,Map<String, String> headers,Class<T> clazz){
        return putSync(url, body, headers).getBody().toBean(clazz);
    }

    /**
     * 发送Put 同步请求 结果序列化为 T
     *
     * @param url       请求Url
     * @param body      请求体
     * @return          T
     */
    public static <T> T putSyncToBean(String url,Map<String, Object> body,Class<T> clazz){
        return putSyncToBean(url, body, null,clazz);
    }



    /**==================================Delete================================================*/

    /**
     * 发送Delete 同步请求
     * @param url       请求Url
     * @param body      请求体
     * @param headers   请求头
     * @return          请求结果
     */
    public static HttpResult deleteSync(String url,Map<String, Object> body,Map<String, String> headers){
        return CLIENT.sync(url).addHeader(headers).addBodyPara(body).delete();
    }


}
