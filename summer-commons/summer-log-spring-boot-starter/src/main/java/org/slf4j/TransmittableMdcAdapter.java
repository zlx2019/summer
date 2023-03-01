package org.slf4j;

import ch.qos.logback.classic.util.LogbackMDCAdapter;
import com.alibaba.ttl.TransmittableThreadLocal;
import org.slf4j.helpers.ThreadLocalMapOfStacks;
import org.slf4j.spi.MDCAdapter;

import java.util.*;

/**
 * 重写{@link LogbackMDCAdapter}类，搭配TransmittableThreadLocal实现父子线程之间的数据传递
 *
 * @author Zero.
 * @date 2023/03/01 20:46:19 8:15
 */
public class TransmittableMdcAdapter implements MDCAdapter {

    // 存放数据的ThreadLocal
    private final ThreadLocal<Map<String, String>> copyOnInheritThreadLocal = new TransmittableThreadLocal<>();
    private final ThreadLocalMapOfStacks threadLocalMapOfDeques = new ThreadLocalMapOfStacks();

    private static final int WRITE_OPERATION = 1;
    private static final int MAP_COPY_OPERATION = 2;

    private static TransmittableMdcAdapter mtcMDCAdapter;

    /**
     * keeps track of the last operation performed
     * 记录上次操作是复制还是写入
     */
    private final ThreadLocal<Integer> lastOperation = new ThreadLocal<>();

    static {
        mtcMDCAdapter = new TransmittableMdcAdapter();
        MDC.mdcAdapter = mtcMDCAdapter;
    }

    public static MDCAdapter getInstance() {
        return mtcMDCAdapter;
    }

    /**
     * 获取上一次操作的类型,将本次操作类型记录下
     * @param op
     * @return
     */
    private Integer getAndSetLastOperation(int op) {
        Integer lastOp = lastOperation.get();
        lastOperation.set(op);
        return lastOp;
    }

    private static boolean wasLastOpReadOrNull(Integer lastOp) {
        return lastOp == null || lastOp == MAP_COPY_OPERATION;
    }

    /**
     * 将旧Map中的数据复制到一个新的Map中
     * @param oldMap    旧map
     * @return          新的Map
     */
    private Map<String, String> duplicateAndInsertNewMap(Map<String, String> oldMap) {
        Map<String, String> newMap = Collections.synchronizedMap(new HashMap<>());
        if (oldMap != null) {
            // we don't want the parent thread modifying oldMap while we are
            // iterating over it
            synchronized (oldMap) {
                newMap.putAll(oldMap);
            }
        }
        copyOnInheritThreadLocal.set(newMap);
        return newMap;
    }

    /**
     * Put a context value (the <code>val</code> parameter) as identified with the
     * <code>key</code> parameter into the current thread's context map. Note that
     * contrary to log4j, the <code>val</code> parameter can be null.
     * <p/>
     * <p/>
     * If the current thread does not have a context map it is created as a side
     * effect of this call.
     *
     * @throws IllegalArgumentException in case the "key" parameter is null
     */
    @Override
    public void put(String key, String val) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        // 获取旧数据
        Map<String, String> oldMap = copyOnInheritThreadLocal.get();
        // 记录本次操作,并且获取上次操作
        Integer lastOp = getAndSetLastOperation(WRITE_OPERATION);
        //
        if (wasLastOpReadOrNull(lastOp) || oldMap == null) {
            Map<String, String> newMap = duplicateAndInsertNewMap(oldMap);
            newMap.put(key, val);
        } else {
            oldMap.put(key, val);
        }
    }

    /**
     * Remove the the context identified by the <code>key</code> parameter.
     * <p/>
     */
    @Override
    public void remove(String key) {
        if (key == null) {
            return;
        }
        Map<String, String> oldMap = copyOnInheritThreadLocal.get();
        if (oldMap == null) {
            return;
        }
        Integer lastOp = getAndSetLastOperation(WRITE_OPERATION);
        if (wasLastOpReadOrNull(lastOp)) {
            Map<String, String> newMap = duplicateAndInsertNewMap(oldMap);
            newMap.remove(key);
        } else {
            oldMap.remove(key);
        }

    }

    /**
     * Clear all entries in the MDC.
     */
    @Override
    public void clear() {
        lastOperation.set(WRITE_OPERATION);
        copyOnInheritThreadLocal.remove();
    }

    /**
     * Get the context identified by the <code>key</code> parameter.
     * <p/>
     */
    @Override
    public String get(String key) {
        final Map<String, String> map = copyOnInheritThreadLocal.get();
        if ((map != null) && (key != null)) {
            return map.get(key);
        } else {
            return null;
        }
    }

    /**
     * Get the current thread's MDC as a map. This method is intended to be used
     * internally.
     */
    public Map<String, String> getPropertyMap() {
        lastOperation.set(MAP_COPY_OPERATION);
        return copyOnInheritThreadLocal.get();
    }

    /**
     * Returns the keys in the MDC as a {@link Set}. The returned value can be
     * null.
     */
    public Set<String> getKeys() {
        Map<String, String> map = getPropertyMap();

        if (map != null) {
            return map.keySet();
        } else {
            return null;
        }
    }

    /**
     * Return a copy of the current thread's context map. Returned value may be
     * null.
     */
    @Override
    public Map<String, String> getCopyOfContextMap() {
        Map<String, String> hashMap = copyOnInheritThreadLocal.get();
        if (hashMap == null) {
            return null;
        } else {
            return new HashMap<>(hashMap);
        }
    }

    @Override
    public void setContextMap(Map<String, String> contextMap) {
        lastOperation.set(WRITE_OPERATION);
        Map<String, String> newMap = Collections.synchronizedMap(new HashMap<>());
        newMap.putAll(contextMap);
        // the newMap replaces the old one for serialisation's sake
        copyOnInheritThreadLocal.set(newMap);
    }

    @Override
    public void pushByKey(String key, String value) {
        threadLocalMapOfDeques.pushByKey(key,value);
    }

    @Override
    public String popByKey(String key) {
        return threadLocalMapOfDeques.popByKey(key);
    }

    @Override
    public Deque<String> getCopyOfDequeByKey(String key) {
        return threadLocalMapOfDeques.getCopyOfDequeByKey(key);
    }


    @Override
    public void clearDequeByKey(String key) {
        threadLocalMapOfDeques.clearDequeByKey(key);
    }
}
