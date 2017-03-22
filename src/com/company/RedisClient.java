package com.company;

/**
 * Created by Nova on 16/9/6.
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.*;

public class RedisClient {

    private static class VALUE_TYPE {
        public static final String NONE = "none";
        public static final String STRING = "string";
        public static final String LIST = "list";
        public static final String SET = "set";
    }
    private Jedis jedisSourse;//非切片额客户端连接
    private Jedis jedisTarget;
    private  Jedis jedis;
    private JedisPool jedisPoolSourse;//非切片连接池
    private JedisPool jedisPoolTarget;
    private ShardedJedis shardedJedis;//切片额客户端连接
    private ShardedJedisPool shardedJedisPool;//切片连接池

    public RedisClient()
    {
        initialPool();
        //initialShardedPool();
        //shardedJedis = shardedJedisPool.getResource();
        jedisSourse = jedisPoolSourse.getResource();
        jedisTarget = jedisPoolTarget.getResource();
    }

    /**
     * 初始化非切片池
     */
    private void initialPool()
    {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(20);
        config.setMaxIdle(50);
        config.setMaxWait(1000l);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        jedisPoolSourse = new JedisPool(config,"192.168.0.52",6382);
        jedisPoolTarget = new JedisPool(config,"127.0.0.1",6379);
        //jedisPoolTarget = new JedisPool(config,"192.168.0.12",6387);
    }

    /**
     * 初始化切片池
     */
    private void initialShardedPool()
    {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(20);
        config.setMaxIdle(5);
        config.setMaxWait(1000l);
        config.setTestOnBorrow(false);
        // slave链接
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379, "master"));

        // 构造池
        shardedJedisPool = new ShardedJedisPool(config, shards);
    }

    public void show() {
        KeyOperate();
        StringOperate();
        /*ListOperate();
        SetOperate();
        SortedSetOperate();
        HashOperate();
        */
        jedisPoolSourse.returnResource(jedisSourse);
        shardedJedisPool.returnResource(shardedJedis);
    }

    protected void transOperate(){
        Set<String> keys = jedisSourse.keys("*");
        Iterator<String> it=keys.iterator() ;
        System.out.println("keys的大小"+keys.size());
        int times = 100;
        int time = 0;
        int count = 0;

        long start = System.currentTimeMillis();
        System.out.println("程序开始时间"+start);
        while (it.hasNext()) {
            try {
                time++;

                if (time > times) {
                    time = 1;
                    count++;
                    /*if(count>20){  //2000条
                        long end = System.currentTimeMillis();
                        System.out.println("消耗时间"+(end-start));
                        break;
                    }*/

                    jedisPoolSourse.returnResource(jedisSourse);
                    jedisPoolTarget.returnResource(jedisTarget);
                    jedisSourse = jedisPoolSourse.getResource();
                    jedisTarget = jedisPoolTarget.getResource();
                }

                String key = it.next();
                String type = jedisSourse.type(key);
                System.out.println(count + " " + time + "个:" + key);
                System.out.println("查看key所储存的值的类型：" + type);

                if (VALUE_TYPE.NONE.equals(type)) {
                    continue;
                }

                if (VALUE_TYPE.SET.equals(type)) {
                    Set<String> values = jedisSourse.smembers(key);
                    for (String value : values) {
                        jedisTarget.sadd(key, value);
                    }
                    continue;
                }

                if (VALUE_TYPE.STRING.equals(type)) {
                    String value = jedisSourse.get(key);

                    jedisTarget.set(key, value);
                    continue;
                }

                if (VALUE_TYPE.LIST.equals(type)) {
                    String value = null;
                    long size = jedisSourse.llen(key);
                    List<String> values = jedisSourse.lrange(key, 0, size);

                    for (int j = 0, length = values.size(); j < length; j++) {
                        jedisTarget.rpush(key, values.get(j));
                    }

                    continue;
                }

           }catch (Exception e){
                time --;
                jedisPoolSourse.returnResource(jedisSourse);
                jedisPoolTarget.returnResource(jedisTarget);
                jedisSourse = jedisPoolSourse.getResource();
                jedisTarget = jedisPoolTarget.getResource();
                e.printStackTrace();
            }
        }


        jedisPoolSourse.returnResource(jedisSourse);
        jedisPoolTarget.returnResource(jedisTarget);
    }

    private void KeyOperate() {
            System.out.println("======================key==========================");
            // 清空数据
            //System.out.println("清空库中所有数据："+jedis.flushDB());
            // 判断key否存在
            //System.out.println("判断key999键是否存在："+shardedJedis.exists("key999"));
            //System.out.println("新增key001,value001键值对："+shardedJedis.set("key001", "value001"));
            System.out.println("判断key001是否存在："+shardedJedis.exists("key001"));
            // 输出系统中所有的key
            System.out.println("新增key002,value002键值对："+shardedJedis.set("key002", "value002"));
            System.out.println("系统中所有键如下：");
            Set<String> keys = jedis.keys("*");
            Iterator<String> it=keys.iterator() ;
            while(it.hasNext()){
                String key = it.next();
                System.out.println(key);
            }
            // 删除某个key,若key不存在，则忽略该命令。
            System.out.println("系统中删除key002: "+jedis.del("key002"));
            System.out.println("判断key002是否存在："+shardedJedis.exists("key002"));
            // 设置 key001的过期时间
            System.out.println("设置 key001的过期时间为5秒:"+jedis.expire("key001", 5));
            try{
                Thread.sleep(2000);
            }
            catch (InterruptedException e){
            }
            // 查看某个key的剩余生存时间,单位【秒】.永久生存或者不存在的都返回-1
            System.out.println("查看key001的剩余生存时间："+jedis.ttl("key001"));
            // 移除某个key的生存时间
            System.out.println("移除key001的生存时间："+jedis.persist("key001"));
            System.out.println("查看key001的剩余生存时间："+jedis.ttl("key001"));
            // 查看key所储存的值的类型
            System.out.println("查看key所储存的值的类型："+jedis.type("key001"));
        /*
         * 一些其他方法：1、修改键名：jedis.rename("key6", "key0");
         *             2、将当前db的key移动到给定的db当中：jedis.move("foo", 1)
         */
        }
    private void StringOperate() {
        System.out.println("=============超过有效期键值对被删除=============");
        // 设置key的有效期，并存储数据
        System.out.println("新增key303，并指定过期时间为2秒"+shardedJedis.setex("key303", 2, "key303-2second"));
        System.out.println("获取key303对应的值："+shardedJedis.get("key303"));
        try{
            Thread.sleep(3000);
        }
        catch (InterruptedException e){
        }
        System.out.println("3秒之后，获取key303对应的值："+shardedJedis.get("key303"));

        System.out.println("=============获取原值，更新为新值一步完成=============");
        System.out.println("key302原值："+shardedJedis.getSet("key302", "value302-after-getset"));
        System.out.println("key302新值："+shardedJedis.get("key302"));

        System.out.println("=============获取子串=============");
        System.out.println("获取key302对应值中的子串："+shardedJedis.getrange("key302", 5, 7));
    }
/*
    private void ListOperate() {
        。。。
    }

    private void SetOperate() {
        。。。
    }

    private void SortedSetOperate() {
        。。。
    }

    private void HashOperate() {
        。。。
    }


*/
}
