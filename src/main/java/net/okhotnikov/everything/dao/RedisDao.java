package net.okhotnikov.everything.dao;

import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * Created by Sergey Okhotnikov.
 */
@Repository
public class RedisDao {

    private final JedisPool pool;

    public RedisDao(JedisPool pool) {
        this.pool = pool;
    }

    public void delKey(String key){
        try (Jedis jedis = pool.getResource()) {
            jedis.del(key);
        }
    }

    public  String getString(String token){
        String res=null;
        try (Jedis jedis = pool.getResource()) {
            res=jedis.get(token);
        }
        return res;
    }

    public void putString(String token, String s,long ttl){
        try (Jedis jedis = pool.getResource()) {
            String res=jedis.setex(token,
                    ttl,s);
        } catch (Exception e){
            throw e;
        }
    }

    public void putString(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set(key, value);
        } catch (Exception e){
            throw e;
        }
    }
}
