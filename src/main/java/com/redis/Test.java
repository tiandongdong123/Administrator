package com.redis;

import java.util.List;
import java.util.Set;


public class Test {
	
	 public static void main(String[] args) {
        RedisUtil redis = new RedisUtil();
        // 操作实体类对象
        for(int i = 1; i <= 100;i+=10){
            // 初始化CommentId索引 SortSet  
            redis.zadd("topicId", i, "commentId"+i);  
            // 初始化Comment数据 Hash  
            redis.hset("Comment_Key","commentId"+i,""); 
        }    
        //倒序取 从0条开始取 5条 Id 数据  
        Set<String> sets = redis.zrevrange("topicId", 0, 5);  
        String[] items = new String[]{};  
        System.out.println(sets.toString());  
        //根据id取comment数据
        List<String> list = redis.hmget("Comment_Key", sets.toArray(items));  
        for(String str : list){  
            System.out.println(str);  
        }  
	 }
}
