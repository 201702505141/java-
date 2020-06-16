package com.lz.service;

import com.lz.config.exception.GlobalException;
import com.lz.config.redis.prefix.MiaoshaUserKey;
import com.lz.dao.MiaoshaUserDao;
import com.lz.entity.MiaoshaUser;
import com.lz.entity.vo.LoginVo;
import com.lz.utils.Md5Utils;
import com.lz.utils.UUIDUtils;
import com.lz.utils.result.CodeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author m
 * @className MiaoShaUserService
 * @description MiaoShaUserService
 * @date 2020/5/13
 */
@Service
public class MiaoshaUserService {

    private static final Logger logger = LoggerFactory.getLogger(MiaoshaUserService.class);

    public static final String COOKIE_NAME_TOKEN = "token";

    @Resource
    private MiaoshaUserDao miaoshaUserDao;

    @Resource
    private RedisService redisService;

    public MiaoshaUser getById(long id) {
        // 取缓存
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById,""+id,MiaoshaUser.class);
        if (user != null ){
            return user;
        }
        // 取数据库，并存入缓存
        user = miaoshaUserDao.getById(id);
        if (user != null ){
            redisService.set(MiaoshaUserKey.getById,""+id, user);
        }
        return user;
    }

    /**
     * 修改密码 更新数据库
     * @param token
     * @param id
     * @param passwordNew
     * @return
     * @throws GlobalException
     */
    public boolean updatePassword(String token,long id,String passwordNew) throws GlobalException {
        //取user
        MiaoshaUser user = getById(id);
        if(user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新的东西越多，产生的lock越多 修改哪个字段，就更新哪个字段
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(Md5Utils.formPassToDBPass(passwordNew,user.getSalt()));
        int val = miaoshaUserDao.update(toBeUpdate);
        if(val > 0){
            //处理缓存 更新缓存
            redisService.delete(MiaoshaUserKey.getById,""+id);
            user.setPassword(toBeUpdate.getPassword());
            redisService.set(MiaoshaUserKey.token,token,user);
            return true;
        }
        return false;
    }

    /**
     * 查看缓存中用户是否已经登录
     * @param response
     * @param token
     * @return MiaoshaUser
     */
    public MiaoshaUser getByToken(HttpServletResponse response,String token) {
        //校验token
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if (user != null){
            addCookie(response, token ,user);
        }
        return user;
    }

    /**
     * 修改 boolean -> Object
     */
    public String doLogin(HttpServletResponse response, LoginVo loginVo) throws GlobalException {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile));
        if (miaoshaUser == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = miaoshaUser.getPassword();
        String saltDB = miaoshaUser.getSalt();
        String calcPass = Md5Utils.formPassToDBPass(formPass, saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtils.uuid();
        addCookie(response, token,miaoshaUser);
        return token;
    }

    private void addCookie(HttpServletResponse response,String token, MiaoshaUser user) {
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
        logger.info("addCookie:->"+"token:" + token + "\t" + "cookie：" + cookie.getMaxAge());

    }

}
