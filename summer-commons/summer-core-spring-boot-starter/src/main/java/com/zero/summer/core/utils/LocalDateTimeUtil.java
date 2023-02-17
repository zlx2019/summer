package com.zero.summer.core.utils;

import com.zero.summer.core.constant.Constant;
import com.zero.summer.core.utils.valid.NullUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * LocalDateTime 工具类
 *
 * @author Zero.
 * @date 2022/1/9 9:08 下午
 */
public class LocalDateTimeUtil {

    private static final DateTimeFormatter DATETIMEFORMATTER = DateTimeFormatter.ofPattern(Constant.DATETIME_FORMAT);
    private static final DateTimeFormatter DATEFORMAT = DateTimeFormatter.ofPattern(Constant.DATE_FORMAT);
    public static final DateTimeFormatter WECHAT_DATEFORMAT = DateTimeFormatter.ofPattern(Constant.WECHAT_DATEFORMAT);

    /**
     * LocalDateTime 格式化: yyyy-MM-dd HH:mm:ss
     *
     * @param time 日期时间
     * @return     text
     */
    public static String formatDateTime(LocalDateTime time) {
        return DATETIMEFORMATTER.format(time);
    }

    /**
     * 微信支付成功时间 转换为LocalDateTime
     * 遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE
     * 示例值：2018-06-08T10:34:56+08:00
     * @param paymentTime 微信支付成功时间字符串
     * @return
     */
    public static LocalDateTime paymentTimeToDate(String paymentTime){
        return LocalDateTime.parse(paymentTime,WECHAT_DATEFORMAT);
    }

    /**
     * Date 转 LocalDateTime
     * @param date  要转换的Date
     * @return      LocalDateTime
     */
    public static LocalDateTime fromDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * LocalDateTime 转 Date
     * @param date  要转换的LocalDateTime
     * @return      Date
     */
    public static Date fromLocalDateTime(LocalDateTime date){
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime 解析
     *
     * @param text 时间文本
     * @return     时间
     */
    public static LocalDateTime parseDateTime(String text) {
        return LocalDateTime.parse(text, DATETIMEFORMATTER);
    }

    /**
     * LocalDate 格式化: yyyy-MM-dd
     * @param date  日期
     * @return      日期text
     */
    public static String formatDate(LocalDate date) {
        return DATEFORMAT.format(date);
    }

    /**
     * LocalDate 解析
     * @param text  日期文本
     * @return      日期
     */
    public static LocalDate parseDate(String text) {
        return LocalDate.parse(text, DATEFORMAT);
    }

    /**
     * 获取当天 00:00:00点时间
     *
     * @param date 当天
     * @return 当天零点日期
     */
    public static LocalDateTime getDayStart(LocalDate date) {
        date = NullUtil.isNullElse(date,LocalDate.now());
        return LocalDateTime.of(date, LocalTime.MIN);
    }

    /**
     * 获取当天 23:59:59点时间
     *
     * @param date 当天
     * @return 当天结束时间
     */
    public static LocalDateTime getDayEnd(LocalDate date) {
        date = NullUtil.isNullElse(date,LocalDate.now());
        return LocalDateTime.of(date, LocalTime.MAX);
    }

    /**
     * 获取当月第一天
     *
     * @param date 当月
     * @return     当月第一天
     */
    public static LocalDate getMonthFirstDay(LocalDate date){
        date = NullUtil.isNullElse(date,LocalDate.now());
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取当月最后一天
     *
     * @param date 当月
     * @return     当月最后一天
     */
    public static LocalDate getMonthEndDay(LocalDate date){
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 根据不同的时间单位,追加时长
     *
     * @param time    日期时间
     * @param number  时长
     * @param unit    时间单位(年月日时分秒) 选自java.time.temporal.ChronoUnit
     * @return        追加后的日期时间
     */
    public static LocalDateTime plus(LocalDateTime time, long number, ChronoUnit unit){
        return time.plus(number,unit);
    }

    /**
     * 根据不同的时间单位,减少时长
     *
     * @param time    日期时间
     * @param number  时长
     * @param unit    时间单位(年月日时分秒) 选自java.time.temporal.ChronoUnit
     * @return        减少后的日期时间
     */
    public static LocalDateTime minus(LocalDateTime time, long number, ChronoUnit unit){
        return time.minus(number,unit);
    }

    /**
     * 获取两个日期的差 field参数为ChronoUnit.*
     *
     * @param startTime 起止时间
     * @param endTime   截止时间
     * @param field     单位(年月日时分秒)
     * @return
     */
    public static long between(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        switch (field){
            case YEARS:
                return period.getYears();
            case MONTHS:
                return period.getYears() * 12L + period.getMonths();
        }
        return field.between(startTime, endTime);
    }
}
