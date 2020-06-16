package com.lz.controller;

import com.lz.config.exception.GlobalException;
import com.lz.config.redis.prefix.UserKey;
import com.lz.entity.vo.LoginVo;
import com.lz.service.MiaoshaUserService;
import com.lz.service.RedisService;
import com.lz.utils.result.CodeMsg;
import com.lz.utils.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author m
 * @className LoginController
 * @description LoginController
 * @date 2020/5/13
 */
@CrossOrigin
@Controller
@Api(value = "登录",description = "登录接口" )
@RequestMapping(value = "login/")
public class LoginController {
    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);
    @Resource
    private MiaoshaUserService miaoshaUserService;
    @Autowired
    RedisService redisService;
    @RequestMapping("toLogin")
    public String toLoginPage(){
        return "login";
    }

    @ApiOperation(httpMethod = "POST",value = "密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "mobile",value = "手机号",required = true ,dataType = "String"),
            @ApiImplicitParam( name = "password",value = "密码",required = true ,dataType = "String")
    })
    @RequestMapping(value = "doLogin")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) throws GlobalException {
        logger.info(loginVo.toString());
        //登录
        String token = miaoshaUserService.doLogin(response,loginVo);
        return Result.success(token);
    }

    @RequestMapping("/mobile")
    public String mobile() {
        return "mobile";
    }

    @RequestMapping(value = "/do_authcode")
    @ResponseBody
    public Result<Boolean> authcode_get(LoginVo loginVo) {
        String mobile=loginVo.getMobile();
        String password = "1" + RandomStringUtils.randomNumeric(5);//生成随机数,我发现生成5位随机数时，如果开头为0，发送的短信只有4位，这里开头加个1，保证短信的正确性
        logger.info(mobile+"\t"+password);
        redisService.set(UserKey.getById,mobile,password);//将验证码存入缓存
//        Message.messagePost(mobile,password);//发送短息
        logger.info("----->"+mobile+"\t"+password);
        return Result.success(true);
    }

    @RequestMapping("/authcode_login")
    @ResponseBody
    public Result<Boolean>  authcode_login(Model model, LoginVo loginVo) {

        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        String verCode = redisService.get(UserKey.getById,mobile, String.class);
//        logger.info("userNickname:"+user.getNickname());
        logger.info("verCode"+verCode);
        if (password.equals(verCode)) {
            return Result.success(true);
        } else {
            return Result.error(CodeMsg.PASSWORD_ERROR);
        }
    }

}
