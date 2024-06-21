package com.yp.CXOJ.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yp.CXOJ.common.BaseResponse;
import com.yp.CXOJ.common.ErrorCode;
import com.yp.CXOJ.common.ResultUtils;
import com.yp.CXOJ.exception.ThrowUtils;
import com.yp.CXOJ.manager.OnlineUserManager;
import com.yp.CXOJ.model.dto.mail.MailBindRequest;
import com.yp.CXOJ.model.dto.mail.YZMLoginRequest;
import com.yp.CXOJ.model.entity.LoginUser;
import com.yp.CXOJ.model.entity.User;
import com.yp.CXOJ.model.vo.LoginUserVO;
import com.yp.CXOJ.service.SendMailService;
import com.yp.CXOJ.service.UserService;
import com.yp.CXOJ.utils.MailUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.yp.CXOJ.config.RedisConfig.redisTemplate;
import static com.yp.CXOJ.constant.UserConstant.USER_LOGIN_STATE;
import static com.yp.CXOJ.service.impl.UserServiceImpl.time;

/**
 * @author 余炎培
 * @since 2024-06-16 星期日 15:29:03
 */
@RestController
@RequestMapping("/sendMail")
@Slf4j
public class SendMailController {

    @Resource
    private SendMailService sendMailService;

    @Resource
    private UserService userService;

    @PostMapping("/getCode")
    @ApiOperation("获取邮箱验证码")
    public BaseResponse<Boolean> sendMailCode(@RequestParam String to, HttpServletRequest request) {
        ThrowUtils.throwIf(!MailUtil.isMail(to), ErrorCode.PARAMS_ERROR, "邮箱格式错误");

        String authCode = String.valueOf(new Random().nextInt(899999) + 100000);//6位数验证码
        //设置缓存的验证码,还没有生成给邮箱to的验证码，则为true,即该邮箱to的验证码没有缓存过,否则为false
        Boolean isCache = redisTemplate.opsForValue()
                .setIfAbsent(to, authCode, 1, TimeUnit.MINUTES);//一分钟
        //不能短时间内重复发送验证码
        ThrowUtils.throwIf(!isCache, ErrorCode.FORBIDDEN_ERROR, "请勿重复发送验证码");

        System.out.println(to);
        sendMailService.sendVerificationCode(to, authCode);

        return ResultUtils.success(true);
    }


    @PostMapping("/login/mail")
    @ApiOperation("邮箱登录")
    public BaseResponse<LoginUserVO> userLoginByMail(@RequestBody YZMLoginRequest yzmLoginRequest, HttpServletRequest request) {
        String mail = yzmLoginRequest.getMail();
        String mailYZM = yzmLoginRequest.getMailYZM();

        //检验邮箱信息
        checkMailInfo(mail,mailYZM);

        //查用户,看邮箱是否被用户绑定过
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address", mail);
        User user = userService.getOne(queryWrapper);
        //没有该用户就注册一个
        if (user == null){
            user = sendMailService.registerUser(mail);
        }

        // 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        request.getSession().setMaxInactiveInterval(time);//设置用户登录时间

        LoginUser loginUser = userService.getLoginUserInfo(user, request);
        OnlineUserManager.addUser(loginUser,request);//添加为已登录用户
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    @PostMapping("/bind/mail")
    @ApiOperation("绑定邮箱")
    public BaseResponse<Boolean> bindMail(@RequestBody MailBindRequest mailBindRequest, HttpServletRequest request) {
        String mail = mailBindRequest.getMail();
        String mailYZM = mailBindRequest.getMailYZM();
        Long userId = mailBindRequest.getUser_id();

        ThrowUtils.throwIf(userId == null || userId <= 0,ErrorCode.PARAMS_ERROR,"用户不存在");

        //检验邮箱信息
        checkMailInfo(mail,mailYZM);

        //查用户,看邮箱是否已被绑定
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address", mail);
        User user = userService.getOne(queryWrapper);
        ThrowUtils.throwIf(user != null && user.getId() > 0, ErrorCode.OPERATION_ERROR, "邮箱已被绑定");

        //该邮箱没有被绑定
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        user = userService.getOne(queryWrapper);
        ThrowUtils.throwIf(user == null || user.getId() <= 0,ErrorCode.NOT_FOUND_ERROR,"用户不存在");

        user.setAddress(mail);
        userService.updateById(user);
        return ResultUtils.success(true);
    }

    //检验邮箱信息
    private void checkMailInfo(String mail,String mailYZM){
        //请求信息判断
        ThrowUtils.throwIf(!MailUtil.isMail(mail), ErrorCode.PARAMS_ERROR, "邮箱格式错误");
        ThrowUtils.throwIf(mail == null, ErrorCode.PARAMS_ERROR, "邮箱为空");
        ThrowUtils.throwIf(mailYZM == null, ErrorCode.PARAMS_ERROR, "验证码为空");

        //验证码验证
        String yzm = redisTemplate.opsForValue().get(mail);
        ThrowUtils.throwIf(yzm == null, ErrorCode.NOT_FOUND_ERROR, "验证码还未获取验证码或已过期");
        ThrowUtils.throwIf(!yzm.equals(mailYZM), ErrorCode.PARAMS_ERROR, "验证码错误");
    }
}
