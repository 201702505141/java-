package com.lz.controller;

import com.lz.config.redis.prefix.UserKey;
import com.lz.entity.User;
import com.lz.service.RedisService;
import com.lz.service.UserService;
import com.lz.utils.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author m
 * @className UserController
 * @description UserController
 * @date 2020/5/11
 */
@RestController
@RequestMapping("user/")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Resource
    private UserService userService;

    @Resource
    private RedisService redisService;

    /**
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public  @ResponseBody
    Result<User> selectOne(Integer id) {
        User user=this.userService.queryById(id);
        return Result.success(user);
    }

    @RequestMapping(value = "redis/get")
    public @ResponseBody
    Result<User> redisGet(){
        User user = redisService.get(UserKey.getById,"8", User.class);
        logger.info(user.toString());
        return Result.success(user);
    }

    @GetMapping(value = "redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user = new User();
        user.setId(8);
        user.setName("iqoo");
        user.setEmail("WWW.iqoo.com");
        boolean key2 = redisService.set(UserKey.getById,"8",user);
//        String user = redisService.get("trump", String.class);
        logger.info(user.toString());
        return Result.success(true);
    }


}