package com.summer.core.utils.snowflakes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * 高效分布式ID生成算法(sequence),基于Snowflake算法优化实现64位自增ID算法。
 * 其中解决时间回拨问题的优化方案如下：
 * 1. 如果发现当前时间少于上次生成id的时间(时间回拨)，着计算回拨的时间差
 * 2. 如果时间差(offset)小于等于5ms，着等待 offset * 2 的时间再生成
 * 3. 如果offset大于5，则直接抛出异常
 *
 */
public class IdGenerator {
    private static final Sequence WORKER = new Sequence();

    public static long getId() {
        return WORKER.nextId();
    }

    public static String getIdStr() {
        return String.valueOf(WORKER.nextId());
    }


    public static String getClassDefaultPassword(){
        String password[] = {"1","2","3","4","5","6","7","8","9",
                "a","b","c","d","e","f","g","h","j","k","m","n","p","q","r","s","t","u","v","w","x","y","z"};

        String passwordString ="";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int a = random.nextInt(password.length);
            passwordString = passwordString + password[a];
        }
        return passwordString;
    }


    public static String getLengthUserName(int size) {
        String idStr = IdGenerator.getIdStr();
        Random random = new Random();
        List<String> stringList = new ArrayList<>(size);
        String[] split = idStr.split("");
        for (int i = 0; i < idStr.split("").length; i++) {
            int index = random.nextInt(size);
            if (stringList.size() >= size){
                break;
            }
            stringList.add(split[index]);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringList.forEach(s -> {
            stringBuffer.append(s);
        });
        return stringBuffer.toString();
    }
}
