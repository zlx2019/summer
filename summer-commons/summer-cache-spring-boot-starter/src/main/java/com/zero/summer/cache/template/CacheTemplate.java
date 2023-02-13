package com.zero.summer.cache.template;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 缓存工具类 封装了Redis 基础API 如有需要可自行扩展
 * @author Zero.
 * @date 2023/2/13 5:47 PM
 */
@Slf4j
public class CacheTemplate {
    /**
     * 默认编码
     */
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * key序列化
     */
    private static final StringRedisSerializer KEY_SERIALIZER = new StringRedisSerializer();

    /**
     * value 序列化
     */
    private static final JdkSerializationRedisSerializer VALUE_SERIALIZER = new JdkSerializationRedisSerializer();

    /**
     * Spring Redis Template
     */
    private RedisTemplate<String, Object> redisTemplate;

    //构造初始化
    public CacheTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setKeySerializer(KEY_SERIALIZER);
        this.redisTemplate.setValueSerializer(VALUE_SERIALIZER);
    }

    /**
     * 获取 RedisSerializer
     */
    protected RedisSerializer<String> getKeySerializer() {
        return redisTemplate.getStringSerializer();
    }
    /** -------------------------------String-----------------------------------------------------*/

    /**
     * 添加缓存
     */
    public Boolean set(final String key,final Object value){
        return redisTemplate.execute((RedisCallback<Boolean>) conn->{
            byte[] keyBytes = getKeySerializer().serialize(key);
            byte[] valueBytes = VALUE_SERIALIZER.serialize(value);
            Boolean result = conn.set(keyBytes, valueBytes);
            return result;
        });
    }

    /**
     * 添加缓存
     */
    public Boolean set(final byte[] key,final byte[] value){
        return redisTemplate.execute((RedisCallback<Boolean>) conn->conn.set(key, value));
    }

    /**
     * 通过 事务 + 乐观锁机制完成数值递增 缓存设置
     * 通过Watch机制,操作前先监听,然后开启事务,在事务执行过程中,如果监听的数据被操作了,此次事务将会失败!
     * @return
     */
    public Boolean incrByTransactionWithLock(final String key, final long value){
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            byte[] keyBytes = KEY_SERIALIZER.serialize(key);
            connection.watch(keyBytes);
            connection.multi();
            connection.incrBy(keyBytes,value);
            List<Object> result = connection.exec();
            return result == null || result.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
        });
    }

    /**
     * 添加新值覆盖旧值,并且获取到旧值
     * 如果key本身就不存在,就会直接添加
     * @param key 主键
     * @param value 新值
     * @return
     */
    public byte[] getOldAndSet(final byte[] key,final byte[] value){
        return redisTemplate.execute((RedisCallback<byte[]>) connection -> {
            byte[] bytes = connection.getSet(key, value);
            return bytes;
        });
    }


    /**
     * 批量添加缓存 mSet
     * @param maps key1:value1 key2:value2 ...
     * @return
     */
    public Boolean batchSet(Map<String,Object> maps){
        return redisTemplate.execute((RedisCallback<Boolean>) conn -> {
            Map<byte[],byte[]> data = new HashMap<>();
            for (Map.Entry<String, Object> entry : maps.entrySet()) {
                data.put(getKeySerializer().serialize(entry.getKey()),VALUE_SERIALIZER.serialize(entry.getValue()));
            }
            Boolean result = conn.mSet(data);
            return result;
        });
    }

    /**
     * 批量添加缓存,原子性,一个失败将全部失败
     * @Author lx Zhang.
     * @Date 2021/5/29 2:07 下午
     * @Param [maps]
     **/
    public Boolean batchSetNx(Map<String, Object> maps){
        return redisTemplate.execute((RedisCallback<Boolean>) conn -> {
            Map<byte[],byte[]> data = new HashMap<>();
            for (Map.Entry<String, Object> entry : maps.entrySet()) {
                data.put(getKeySerializer().serialize(entry.getKey()),VALUE_SERIALIZER.serialize(entry.getValue()));
            }
            return conn.mSetNX(data);
        });
    }

    /**
     * 批量获取
     * @param keys key列表
     * @return
     */
    public List<Object> batchGet(List<String> keys){
        List<byte[]> list = redisTemplate.execute((RedisCallback<List<byte[]>>) conn -> {
            byte[][] bytes = new byte[keys.size()][];
            for (int i = 0; i < keys.size(); i++) {
                bytes[i] = KEY_SERIALIZER.serialize(keys.get(i));
            }
            return conn.mGet(bytes);
        });
        List<Object> objects = list.stream().map(item -> VALUE_SERIALIZER.deserialize(item)).collect(Collectors.toList());
        return objects;
    }


    /**
     * 添加缓存 设置有效时间(<byte[],byte[]>)
     *
     * @param key   键
     * @param value 值
     * @param time 有效时间
     * @return
     */
    public Boolean setExpire(final byte[] key,final byte[] value,final long time){
        return redisTemplate.execute((RedisCallback<Boolean>) conn->conn.setEx(key,time,value));
    }

    /**
     * 添加缓存 设置有效时间(<String,Object>)
     * @param key   键
     * @param value 值
     * @param time  有效时间
     * @return
     */
    public Boolean setExpire(final String key,final Object value,final long time){
        return redisTemplate.execute((RedisCallback<Boolean>) conn->{
            byte[] keyBytes = getKeySerializer().serialize(key);
            byte[] valueBytes = VALUE_SERIALIZER.serialize(value);
            return conn.setEx(keyBytes,time,valueBytes);
        });
    }

    /**
     * 批量添加缓存
     * @param keys   键列表
     * @param values 值列表
     * @return
     */
    public Boolean setExpireList(final List<String> keys, final List<Object> values){
        return redisTemplate.execute((RedisCallback<Boolean>) conn->{
            RedisSerializer<String> keySerializer = getKeySerializer();
            List<Boolean> resultList = new ArrayList<>();
            for (int i = 0;i < keys.size();i++){
                byte[] keyBytes = keySerializer.serialize(keys.get(i));
                byte[] valueBytes = VALUE_SERIALIZER.serialize(values.get(i));
                resultList.add(conn.set(keyBytes,valueBytes));
            }
            return resultList.stream().allMatch(Boolean::booleanValue);
        });
    }

    /**
     * 批量添加缓存
     * @param keys   键列表
     * @param values 值列表
     * @param time   有效时间
     * @return
     */
    public Boolean setExpireList(final List<String> keys, final List<Object> values, final long time){
        return redisTemplate.execute((RedisCallback<Boolean>) conn->{
            RedisSerializer<String> keySerializer = getKeySerializer();
            List<Boolean> resultList = new ArrayList<>();
            for (int i = 0;i < keys.size();i++){
                byte[] keyBytes = keySerializer.serialize(keys.get(i));
                byte[] valueBytes = VALUE_SERIALIZER.serialize(values.get(i));
                resultList.add(conn.setEx(keyBytes,time,valueBytes));
            }
            return resultList.stream().allMatch(Boolean::booleanValue);
        });
    }

    /**
     * 根据key,获取剩余的有效时间
     * @param key 键
     * @param unit 返回时间单位
     **/
    public Long getExpire(final String key,TimeUnit unit){
        return redisTemplate.execute((RedisCallback<Long>)connection ->{
            RedisSerializer<String> serializer = getKeySerializer();
            return connection.ttl(serializer.serialize(key),unit);
        });
    }

    /**
     * 查询在以keyPatten的所有  key (不推荐使用)
     * @param keyPatten the key patten
     * @return the set
     */
    public Set<String> keys(final String keyPatten) {
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> redisTemplate.keys(keyPatten + "*"));
    }

    /**
     * 根据Key前缀 模糊匹配所有Key(推荐)
     * @param key 键
     * @return 所有Key
     */
    public Set<String> scanKey(final String key){
        return redisTemplate.execute((RedisCallback<Set<String>>) conn->{
            Set<String> keys = new HashSet<>();
            ScanOptions build = ScanOptions.scanOptions().match(key + "*")
                    .count(Integer.MAX_VALUE).build();
            Cursor<byte[]> scan = conn.scan(build);
            while (scan.hasNext()){
                keys.add(KEY_SERIALIZER.deserialize(scan.next()));
            }
            return keys;
        });
    }


    /**
     * 根据key匹配获取对象
     *
     * @param keyPatten the key patten
     * @return the keys values
     */
    public Map<String, Object> getKeysValues(final String keyPatten) {
        log.debug("[redisTemplate redis]  getValues()  patten={} ", keyPatten);
        return redisTemplate.execute((RedisCallback<Map<String, Object>>) connection -> {
            RedisSerializer<String> serializer = getKeySerializer();
            Map<String, Object> maps = new HashMap<>(16);
            Set<String> keys = redisTemplate.keys(keyPatten + "*");
            if (CollectionUtils.isNotEmpty(keys)) {
                for (String key : keys) {
                    byte[] bKeys = serializer.serialize(key);
                    byte[] bValues = connection.get(bKeys);
                    Object value = VALUE_SERIALIZER.deserialize(bValues);
                    maps.put(key, value);
                }
            }
            return maps;
        });
    }

    /**
     * 根据Key 获取 value
     * @param key
     * @return
     */
    public Object get(final String key){
        return redisTemplate.execute((RedisCallback<Object>) connection->{
            byte[] value = connection.get(getKeySerializer().serialize(key));
            return VALUE_SERIALIZER.deserialize(value);
        });
    }

    /**
     * 根据key 获取 value
     * @param key
     * @return
     */
    public byte[] get(final byte[] key){
         return redisTemplate.execute((RedisCallback<byte[]>) conn->conn.get(key));
    }



    /**
     * 判断某个主键是否存在
     *
     * @param key the key
     * @return the boolean
     */
    public boolean exists(final String key) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.exists(key.getBytes(DEFAULT_CHARSET)));
    }

    /**
     * 删除key
     *
     * @param keys the keys
     * @return the long
     */
    public long del(final String... keys) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            long result = 0;
            for (String key : keys) {
                result = connection.del(key.getBytes(DEFAULT_CHARSET));
            }
            return result;
        });
    }

    /**
     * 对某个主键对应的值加一,value值必须是全数字的字符串
     *
     * @param key the key
     * @return the long
     */
    public long incr(final String key) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> redisSerializer = getKeySerializer();
            return connection.incr(redisSerializer.serialize(key));
        });
    }



    /** ----------------------------Hash--------------------------------------------------------*/
    /**
     * Ops for hash hash operations.
     * 获取Hash 引擎
     *
     * @return the hash operations
     */
    public HashOperations<String, String, Object> opsForHash() {
        return redisTemplate.opsForHash();
    }

    /**
     * 对HashMap操作
     *
     * @param key       the key
     * @param hashKey   the hash key
     * @param hashValue the hash value
     */
    public void putHashValue(String key, String hashKey, Object hashValue) {
        log.debug("[redisTemplate redis]  putHashValue()  key={},hashKey={},hashValue={} ", key, hashKey, hashValue);
        opsForHash().put(key, hashKey, hashValue);
    }

    /**
     * 获取单个field对应的值
     *
     * @param key     the key
     * @param hashKey the hash key
     * @return the hash values
     */
    public Object getHashValues(String key, String hashKey) {
        log.debug("[redisTemplate redis]  getHashValues()  key={},hashKey={}", key, hashKey);
        return opsForHash().get(key, hashKey);
    }

    /**
     * 根据key值删除
     *
     * @param key      the key
     * @param hashKeys the hash keys
     */
    public void delHashValues(String key, Object... hashKeys) {
        log.debug("[redisTemplate redis]  delHashValues()  key={}", key);
        opsForHash().delete(key, hashKeys);
    }

    /**
     * key只匹配map
     *
     * @param key the key
     * @return the hash value
     */
    public Map<String, Object> getHashValue(String key) {
        log.debug("[redisTemplate redis]  getHashValue()  key={}", key);
        return opsForHash().entries(key);
    }

    /**
     * 批量添加
     *
     * @param key the key
     * @param map the map
     */
    public void putHashValues(String key, Map<String, Object> map) {
        opsForHash().putAll(key, map);
    }


    /**
     * Hash 结构
     *
     * 查看Hash中是否存在这个 field
     * @param key
     * @param hashKey
     * @return
     */
    public boolean hashExists(String key,String hashKey){
        return opsForHash().hasKey(key,hashKey);
    }

    /**
     * Hash 结构
     *
     * 获取Hash的所有field
     * @param key
     * @return
     */
    public Set<String> hashKeys(final String key){
         return opsForHash().keys(key);
    }

    /**
     * Hash 结构
     *
     * 获取Hash的所有value
     * @param key
     * @return
     */
    public List<Object> hashValues(final String key){
        return opsForHash().values(key);
    }


    /** ------------------------List-------------------------------------------------------*/

    /**
     * redis List 引擎
     *
     * @return the list operations
     */
    public ListOperations<String, Object> opsForList() {
        return redisTemplate.opsForList();
    }

    /**
     * redis List数据结构 : 将一个或多个值 value 插入到列表 key 的表头
     *
     * @param key   the key
     * @param value the value
     * @return the long
     */
    public Long leftPush(String key, Object value) {
        return opsForList().leftPush(key, value);
    }

    /**
     * redis List数据结构 : 移除并返回列表 key 的头元素
     *
     * @param key the key
     * @return the string
     */
    public Object leftPop(String key) {
        return opsForList().leftPop(key);
    }

    /**
     * redis List数据结构 :将一个或多个值 value 插入到列表 key 的表尾(最右边)。
     *
     * @param key   the key
     * @param value the value
     * @return the long
     */
    public Long in(String key, Object value) {
        return opsForList().rightPush(key, value);
    }

    /**
     * redis List数据结构 : 移除并返回列表 key 的末尾元素
     *
     * @param key the key
     * @return the string
     */
    public Object rightPop(String key) {
        return opsForList().rightPop(key);
    }


    /**
     * redis List数据结构 : 返回列表 key 的长度 ; 如果 key 不存在，则 key 被解释为一个空列表，返回 0 ; 如果 key 不是列表类型，返回一个错误。
     *
     * @param key the key
     * @return the long
     */
    public Long length(String key) {
        return opsForList().size(key);
    }


    /**
     * redis List数据结构 : 根据参数 i 的值，移除列表中与参数 value 相等的元素
     *
     * @param key   the key
     * @param i     the
     * @param value the value
     */
    public void remove(String key, long i, Object value) {
        opsForList().remove(key, i, value);
    }

    /**
     * redis List数据结构 : 将列表 key 下标为 index 的元素的值设置为 value
     *
     * @param key   the key
     * @param index the index
     * @param value the value
     */
    public void set(String key, long index, Object value) {
        opsForList().set(key, index, value);
    }

    /**
     * redis List数据结构 : 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 end 指定。
     *
     * @param key   the key
     * @param start the start
     * @param end   the end
     * @return the list
     */
    public List<Object> getList(String key, int start, int end) {
        return opsForList().range(key, start, end);
    }

    /**
     * redis List数据结构 : 批量存储
     *
     * @param key  the key
     * @param list the list
     * @return the long
     */
    public Long leftPushAll(String key, List<String> list) {
        return opsForList().leftPushAll(key, list);
    }

    /**
     * redis List数据结构 : 将值 value 插入到列表 key 当中，位于值 index 之前或之后,默认之后。
     *
     * @param key   the key
     * @param index the index
     * @param value the value
     */
    public void insert(String key, long index, Object value) {
        opsForList().set(key, index, value);
    }

    /** -------------------------------------Set-------------------------------------------------*/

    /**
     * 获取Set结构引擎
     * @return
     */
    public SetOperations<String,Object> opsForSet(){
        return redisTemplate.opsForSet();
    }

    /**
     * Set 结构
     *
     * 添加缓存
     * @param key
     * @param values
     * @return 添加成功数量
     */
    public Long setAdd(final String key,List<Object> values){
        Long successCount = opsForSet().add(key, values.toArray(new Object[values.size()]));
        return successCount;
    }

    /**
     * Set 结构
     *
     * 获取列表
     * @param key
     * @return
     */
    public Set<Object> setMembers(final String key){
        return opsForSet().members(key);
    }

    /**
     * Set 结构
     *
     * 查看value在key对应的无序列表中是否存在
     * @param key
     * @param value
     * @return
     */
    public boolean setExistMember(final String key,final Object value){
        return opsForSet().isMember(key,value);
    }

    /**
     * Set结构
     *
     * 从列表中删除值
     * @param key
     * @param values
     * @return 删除成功的数量
     */
    public Long setRemove(final String key,final List<Object> values){
        return opsForSet().remove(key,values.toArray(new Object[values.size()]));
    }

    /**
     * Set结构
     *
     * 从列表中随机获取并且弹出一个值
     * @param key
     * @return
     */
    public Object setRandomPop(final String key){
        return opsForSet().pop(key);
    }

    /**
     * Set结构
     *
     * 从列表中随机获取n个值,但不会删除
     * @param key
     * @param count
     * @return
     */
    public List<Object> setRandomPop(final String key,final Long count){
        return opsForSet().randomMembers(key,count);
    }

    /**
     * Set 结构
     *
     * 将一个列表中指定的值,移动到另一个列表中 值必须存在
     * @param fromKey 源
     * @param toKey 目标
     * @param value 值
     */
    public boolean setMove(final String fromKey,final String toKey,final Object value){
        return opsForSet().move(fromKey,value,toKey);
    }

    /**
     * Set 结构
     *
     * 获取多个列表之间的数据交集(多个列表中都存在的数据)
     * @param keys
     * @return
     */
    public Set<Object> setSinter(final Collection<String> keys){
        return opsForSet().intersect(keys);
    }

    /**
     * Set 结构
     *
     * 获取多个列表之间的数据交集,保存至另一个Set列表中
     * @param keys
     * @param toKey
     * @return
     */
    public Long setSinter(final Collection<String> keys,final String toKey){
        return opsForSet().intersectAndStore(keys,toKey);
    }

    /**
     * Set 结构
     *
     * 获取多个列表之间的数据并集(多个列表数据的合并)
     * @param keys
     * @return
     */
    public Set<Object> setUnion(final Collection<String> keys){
        return opsForSet().union(keys);
    }

    /**
     * Set 结构
     *
     * 获取多个列表之间的数据并集,保存至另一个列表中
     * @param keys
     * @param toKey
     * @return
     */
    public Long setUnion(final Collection<String> keys,final String toKey){
        return opsForSet().unionAndStore(keys,toKey);
    }


    /**
     * Set 结构
     * 获取 key1和key2的差集(key1中包含,但是key2中没有的数据)
     *  key1: 1,2,4,7
     *  key2: 1,2
     *  差集: 4,7
     * @param key1
     * @param key2
     * @return
     */
    public Set<Object> setDifference(final String key1,final String key2){
        return opsForSet().difference(key1,key2);
    }

    /**
     * Set 结构
     * 获取 key1和key2的差集 保存至 toKey中
     * @param key1
     * @param key2
     * @param toKey
     * @return
     */
    public Long setDifference(final String key1,final String key2,String toKey){
         return opsForSet().differenceAndStore(key1,key2,toKey);
    }


    /**
     * Set结构
     *
     * 获取这个无序列表的数据个数
     * @param key
     * @return
     */
    public Long setCount(final String key){
        return opsForSet().size(key);
    }


    /** ---------------------------------ZSet-------------------------------------------*/

    /**
     * 获取ZSet 引擎
     * @return
     */
    public ZSetOperations opsForZSet(){
        return redisTemplate.opsForZSet();
    }

    /**
     * ZSet 结构
     *
     * 添加缓存
     * @param key
     * @param value
     * @param score 排序量
     * @return
     */
    public Boolean zSetAdd(final String key,final Object value,final Double score){
        return opsForZSet().add(key,value,score);
    }

    /**
     * ZSet 结构
     *
     * 批量添加缓存
     * @param key
     * @param values
     * @return
     */
    public Long zSetAdd(final String key,Set<DefaultTypedTuple<Object>> values){
         return opsForZSet().add(key,values);
    }

    /**
     * ZSet 结构
     *
     * 获取 列表数据
     * @param key
     * @param start 起始Index
     * @param end   结尾Index
     * @param desc true: 降序获取  false: 升序获取  默认升序
     * @return
     */
    public Set zSetRange(final String key,Long start,Long end,boolean desc){
        start = Optional.ofNullable(start).orElse(0L);
        end = Optional.ofNullable(end).orElse(-1L);
        if (desc){
            return opsForZSet().reverseRange(key,start,end); //降序列表
        }
        return opsForZSet().range(key,start,end); //升序列表
    }

    /**
     * ZSet 结构
     *
     * 获取 列表数据 携带score
     * @param key
     * @param start 起始Index
     * @param end   结尾Index
     * @param desc true: 降序获取  false: 升序获取  默认升序
     * @return
     */
    public Set<DefaultTypedTuple> zSetRangeWithScore(final String key,Long start,Long end,boolean desc){
        start = Optional.ofNullable(start).orElse(0L);
        end = Optional.ofNullable(end).orElse(-1L);
        if (desc){
            return opsForZSet().reverseRangeWithScores(key,start,end);
        }
        return opsForZSet().rangeWithScores(key,start,end);
    }

    /**
     * ZSet 结构
     *
     * 范围获取 获取score为 min 至 max 之间的数据
     * @param key
     * @param min 最小值
     * @param max 最大值
     * @param desc true: 降序获取  false: 升序获取  默认升序
     * @return
     */
    public Set zSetRangeByScore(final String key,Double min,Double max,boolean desc){
        if (desc){
            opsForZSet().reverseRangeByScore(key,min,max);
        }
        return opsForZSet().rangeByScore(key,min,max);
    }

    /**
     * ZSet 结构
     *
     * 范围获取 获取score为 min 至 max 之间的数据 携带scores
     * @param key
     * @param min 最小值
     * @param max 最大值
     * @param desc true: 降序获取  false: 升序获取  默认升序
     * @return
     */
    public Set<DefaultTypedTuple> zSetRangeByScoreWithScore(final String key,Double min,Double max,boolean desc){
        if (desc){
            return opsForZSet().reverseRangeByScoreWithScores(key,min,max);
        }
        return opsForZSet().rangeByScoreWithScores(key,min,max);
    }

    /**
     * ZSet 结构
     * score 增加
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Double zSetIncrementScore(final String key,Object value,Double score){
        return opsForZSet().incrementScore(key,value,score);
    }

    /**
     * ZSet 结构
     *
     * 获取一个值在列表中的排名
     * @param key
     * @param value
     * @return
     */
    public Long zSetRankByValue(final String key,final Object value){
        return opsForZSet().rank(key,value);
    }


    /** ----------------------------消息的发布-------------------------------------------------*/

    /**
     * 发布消息
     *
     * @param channel 消息的频道key
     * @param message 消息内容
     * @param author  发布者
     */
//    public void publish(final String channel,final String message,final String author){
//        //封装成消息对象
//        CacheMessage msg = new CacheMessage(author, message, LocalDateTime.now());
//        redisTemplate.convertAndSend(new ChannelTopic(channel).getTopic(), JSON.toJSONString(msg));
//    }

    /** -------------------------------BitMap-----------------------------------------------------*/

    /**
     * BitMaps 结构
     *
     * 设置Bit位为1
     * @param key 键
     * @param offset 偏移量
     * @return
     */
    public Boolean setBit(final String key,Long offset){
        return redisTemplate.execute((RedisCallback<Boolean>) conn -> conn.setBit(KEY_SERIALIZER.serialize(key),offset,Boolean.TRUE));
    }

    /**
     * BitMaps 结构
     *
     * 获取Bit位的值--> true: 1 false: 0
     * @param key 键
     * @param offset 偏移量
     * @return
     */
    public Boolean getBit(final String key,Long offset){
        return redisTemplate.execute((RedisCallback<Boolean> )conn -> conn.getBit(KEY_SERIALIZER.serialize(key),offset));
    }

    /**
     * BitMaps 结构
     *
     * 统计bit位为1的数量
     * @param key
     * @return
     */
    public Long bitCount(final String key){
        return redisTemplate.execute((RedisCallback<Long>) conn-> conn.bitCount(KEY_SERIALIZER.serialize(key)));
    }

    /**
     * BitMaps 位运算
     * @param deskKey 结果Key
     * @param keys 运算的Bit Key
     * @param type 运算类型
     * @return
     */
    public Long bitOp(String deskKey,Collection<String> keys,RedisStringCommands.BitOperation type){
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
           return connection.bitOp(type,KEY_SERIALIZER.serialize(deskKey),keys.stream().map(KEY_SERIALIZER::serialize).toArray(byte[][]::new));
        });
    }


    /**
     * 对多个bitmap 进行 与 运算 &  将结果缓存至另一个Key
     * 第一个操作数的每一位和第二个操作数的每一位对比，如果都是1，那么结果1，否则为0
     *  0 1 0 0 1
     *  0 0 0 0 1
     *  --------- 运算后
     *  0 0 0 0 1
     * @param deskKey 结果Key
     * @param keys    要运算的BitMap Key
     * @return
     */
    public Long bitOpForAnd(String deskKey,List<String> keys){
        return redisTemplate.execute((RedisCallback<Long>) conn-> {
            byte[] deskByte = KEY_SERIALIZER.serialize(deskKey);
            byte[][] keysBytes = keys.stream().map(KEY_SERIALIZER::serialize).toArray(byte[][]::new);
            return conn.bitOp(RedisStringCommands.BitOperation.AND,deskByte,keysBytes);
        });
    }

    /**
     * 对多个bitmap 进行 或 运算 &  将结果缓存至另一个Key
     * 第一个操作数的每一位和第二个操作数的每一位对比，只要有一个是1，那么结果1，否则为0
     *  0 1 0 0 1
     *  0 0 1 0 1
     *  --------- 运算后
     *  0 1 1 0 1
     * @param deskKey 结果Key
     * @param keys 要运算的BitMap Key
     * @return
     */
    public Long bitOpForOr(String deskKey,List<String> keys){
        return redisTemplate.execute((RedisCallback<Long>) conn-> {
            byte[] deskByte = KEY_SERIALIZER.serialize(deskKey);
            byte[][] keysBytes = keys.stream().map(KEY_SERIALIZER::serialize).toArray(byte[][]::new);
           return conn.bitOp(RedisStringCommands.BitOperation.OR,deskByte,keysBytes);
        });
    }


    /**
     * 对多个bitmap 进行 异或 运算 &  将结果缓存至另一个Key
     * 将两个bit位数进行对比,相同 为0,反之为1
     *  0 1 0 0 1
     *  0 0 1 0 1
     *  --------- 运算后
     *  1 0 0 1 1
     * @param deskKey
     * @param keys
     * @return
     */
    public Long bitOpForXOR(String deskKey,List<String> keys){
        return redisTemplate.execute((RedisCallback<Long>) conn -> {
            byte[] deskByte = KEY_SERIALIZER.serialize(deskKey);
            byte[][] keysBytes = keys.stream().map(KEY_SERIALIZER::serialize).toArray(byte[][]::new);
            return conn.bitOp(RedisStringCommands.BitOperation.XOR,deskByte,keysBytes);
        });
    }

    /**
     * 对BitMap进行(!)取反
     * 将操作位进行取反, 0->1; 1->0
     *  0 1 0 0 1
     *  --------- 运算后
     *  1 0 1 1 0
     * @param deskKey 结果Key
     * @param key     要取反的BitMap Key
     * @return
     */
    public Long bitOpForNot(String deskKey,String key){
        return redisTemplate.execute((RedisCallback<Long>) conn->{
            byte[] deskByte = KEY_SERIALIZER.serialize(deskKey);
            byte[] keyByte = KEY_SERIALIZER.serialize(key);
            return conn.bitOp(RedisStringCommands.BitOperation.NOT,deskByte,keyByte);
        });
    }


    /** -------------------------------HyperLogLog 基数结构-----------------------------------------------------*/

    /**
     * HyperLogLog 结构
     *
     * 添加一个或多个数据
     * @param key
     * @param values
     * @return
     */
    public Long pfAdd(final String key,List<Object> values){
        return redisTemplate.execute((RedisCallback<Long>) conn->{
            byte[][] bytes = values.stream().map(item -> VALUE_SERIALIZER.serialize(item)).toArray(byte[][]::new);
            return conn.pfAdd(KEY_SERIALIZER.serialize(key),bytes);
        });
    }

    /**
     * HyperLogLog 结构
     *
     * 统计HyperLogLog 数量(不重复的基数)
     * @param key
     * @return
     */
    public Long pfCount(final String key){
        return redisTemplate.execute((RedisCallback<Long>) conn ->{
           return conn.pfCount(KEY_SERIALIZER.serialize(key));
        });
    }

    /**
     * 合并多个HyperLogLog 到一个新的HyperLogLog
     * @param sourceKeys
     * @param toKey
     * @return
     */
    public Boolean pfMerge(Collection<String> sourceKeys, final String toKey){
         return redisTemplate.execute((RedisCallback<Boolean>) conn-> {
            byte[][] keys = sourceKeys.stream().map(key -> KEY_SERIALIZER.serialize(key)).toArray(byte[][]::new);
            conn.pfMerge(KEY_SERIALIZER.serialize(toKey),keys);
            return Boolean.TRUE;
        });
    }



    /** -------------------------------Geospatial 经纬度结构-----------------------------------------------------*/

    /**
     * GEO 结构
     *
     * 添加 geo坐标缓存
     * @param key 键
     * @param x X轴坐标
     * @param y Y轴坐标
     * @param address 地名
     * @return
     */
    public Long geoAdd(final String key,double x,double y,final String address){
        return redisTemplate.execute((RedisCallback<Long>) conn-> {
            return conn.geoAdd(KEY_SERIALIZER.serialize(key),new Point(x, y),KEY_SERIALIZER.serialize(address));
        });
    }

    /**
     *GEO 结构
     *
     * 批量添加坐标缓存
     * @param key
     * @param maps
     * @return
     */
    public Long geoAdd(final String key,Map<String,Point> maps){
        return redisTemplate.execute((RedisCallback<Long>) conn-> {
            Map<byte[],Point> values = new HashMap<>(maps.size());
            for (Map.Entry<String, Point> entry : maps.entrySet()) {
                values.put(KEY_SERIALIZER.serialize(entry.getKey()),entry.getValue());
            }
            return conn.geoAdd(KEY_SERIALIZER.serialize(key),values);
        });
    }

    /**
     * GEO 结构
     *
     * 获取一个地址的经纬度
     * @param key
     * @param address
     * @return
     */
    public Point geoPos(final String key,final String address){
        return redisTemplate.execute((RedisCallback<Point>) conn-> {
            Optional<Point> optional = conn.geoPos(KEY_SERIALIZER.serialize(key), KEY_SERIALIZER.serialize(address)).stream().findFirst();
            return optional.isPresent() ? optional.get() : null;
        });
    }

    /**
     * GEO结构
     *
     * 获取多个地址的经纬度
     * @param key
     * @param address
     * @return
     */
    public List<Point> geoPos(final String key,final List<String> address){
        return redisTemplate.execute((RedisCallback<List<Point>>) conn-> {
            byte[][] addressBytes = address.stream().map(item -> KEY_SERIALIZER.serialize(item)).toArray(byte[][]::new);
            return conn.geoPos(KEY_SERIALIZER.serialize(key),addressBytes);
        });
    }

    /**
     * GEO 结构
     *
     * 获取两个位置之间的直线距离
     * @param key           键
     * @param addressForm   位置名称1
     * @param addressTo     位置名称2
     * @param unit          距离单位,默认为米
     * @return
     */
    public Double geoDist(final String key,final String addressForm,final String addressTo ,Metric unit){
        unit = Optional.ofNullable(unit).orElse(RedisGeoCommands.DistanceUnit.METERS);
        Metric finalUnit = unit;
        return redisTemplate.execute(((RedisCallback<Double>) connection -> {
            Distance distance = connection.geoDist(KEY_SERIALIZER.serialize(key),KEY_SERIALIZER.serialize(addressForm),
                    KEY_SERIALIZER.serialize(addressTo), finalUnit);
            return distance.getValue();
        }));
    }


    /**
     * 集合数量
     *
     * @return the long
     */
    public long dbSize() {
        return redisTemplate.execute(RedisServerCommands::dbSize);
    }

    /**
     * 清空redis存储的数据
     *
     * @return the string
     */
    public String flushDB() {
        return redisTemplate.execute((RedisCallback<String>) connection -> {
            connection.flushDb();
            return "ok";
        });
    }

    /**
     * 清空DB
     * @param node redis 节点
     */
    public void flushDB(RedisClusterNode node) {
        this.redisTemplate.opsForCluster().flushDb(node);
    }

}
