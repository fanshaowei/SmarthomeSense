package cn.com.papi.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sun on 2015/9/7 0007.
 */
public class RedisUtil {

    private RedisTemplate<String, Object> redisTemplate ;
    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    public RedisUtil(RedisTemplate<String,Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }


    public void add(String key, Object obj){
        try {
            redisTemplate.opsForValue().set(key, obj);
        } catch (Exception e) {
            logger.error("Failed adding {}", obj, e);
            e.printStackTrace();
        }
    }

    public void addObject(String key, Object obj, long timeout) {
        try {
            redisTemplate.opsForValue().set(key, obj, timeout);
        } catch (Exception e) {
            logger.error("Failed adding {}", obj, e);
            e.printStackTrace();
        }
    }

    /**
     * 添加
     * @param key   
     * @param obj   
     * @param timeout   过期时间
     * @param timeunit  时间单位
     */
    public void add(String key, Object obj, long timeout, TimeUnit timeunit ) {
        try {
            redisTemplate.opsForValue().set(key, obj, timeout, timeunit);
        } catch (Exception e) {
            logger.error("Failed adding {}", obj, e);
            e.printStackTrace();
        }
    }

    /**
     * 删除
     * @param key  
     * @return  true or false
     */
    public boolean deleteObject(String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Exception e) {
            logger.error("Failed deleting {}", key, e);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取
     * @param key 
     * @return 
     */
    public Object get(String key) {
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj != null){
                return obj;
            }
        } catch (Exception e) {
            logger.error("Failed getting {}", key, e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新
     * @param key 
     * @param obj 
     */
    public void update(String key, Object obj) {
        try {
            redisTemplate.delete(key);
            redisTemplate.opsForValue().set(key, obj);
        } catch (Exception e){
            logger.error("Failed updating {}",key,obj);
            e.printStackTrace();
        }
    }

    /**
     * 获取存活时间
     * @param key 
     * @return 
     */
    public Long getExpireTime(String key) {
        try {
            long timeout = redisTemplate.getExpire(key);
            return timeout;
        } catch (Exception e) {
            logger.error("Failed get expire time {}", key, e);
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 加入队列
     *
     * @param key
     * @param value
     * @return
     */
    public Long push(String key, Object value) {
        try {
            return redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            logger.error("Failed push {}", key, value);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 出列
     *
     * @param key
     * @return
     */
    public Object pop(String key) {
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            logger.error("Failed pop {}", key);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 进栈
     *
     * @param key
     * @param value
     * @return
     */
    public Long in(String key, Object value) {
        try {
            return redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            logger.error("Failed in {}" , key ,value);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 出栈
     *
     * @param key
     * @return
     */
    public Object out(String key) {
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            logger.error("Failed out {}", key);
            e.printStackTrace();
        }
        return null;

    }

    /**
     *
     * @param key
     * @return
     */
    public Long length(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            logger.error("Failed length {}", key);
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<Object> range(String key, int start, int end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            logger.error("Failed range {}",key);
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param key
     * @param i
     * @param value
     */
    public void remove(String key, long i, Object value) {
        try {
            redisTemplate.opsForList().remove(key, i, value);
        } catch (Exception e) {
            logger.error("Failed remove {}", key);
            e.printStackTrace();
        }
    }

    /**
     *
     * @param key
     * @param index
     * @return
     */
    public String index(String key, long index) {
        try {
            return (String) redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            logger.error("Failed index {}", key, index);
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param key
     * @param index
     * @param value
     */

    public void set(String key, long index, String value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
        } catch (Exception e) {
            logger.error("Failed set {}",key, index);
            e.printStackTrace();
        }
    }

    /**
     *
     * @param key
     * @param start
     * @param end
     */
    public void trim(String key, long start, int end) {
        redisTemplate.opsForList().trim(key, start, end);
    }
    






}
