package com.yp.CXOJ.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yp.CXOJ.common.ErrorCode;
import com.yp.CXOJ.exception.BusinessException;
import com.yp.CXOJ.exception.ThrowUtils;
import com.yp.CXOJ.manager.OnlineUserManager;
import com.yp.CXOJ.model.entity.LoginUser;
import com.yp.CXOJ.model.entity.User;
import com.yp.CXOJ.model.enums.UserRoleEnum;
import com.yp.CXOJ.model.vo.LoginUserVO;
import com.yp.CXOJ.service.UserService;
import com.yp.CXOJ.service.WxMpLoginService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.UUID;

import static com.yp.CXOJ.constant.UserConstant.USER_LOGIN_STATE;
import static com.yp.CXOJ.service.impl.UserServiceImpl.SALT;
import static com.yp.CXOJ.service.impl.UserServiceImpl.time;

/**
 * @author 余炎培
 * @since 2024-06-08 星期六 12:24:36
 */
@Slf4j
@Service
public class WxMpLoginServiceImpl implements WxMpLoginService {
    @Resource
    private WxMpService wxMpService;

    @Resource
    private UserService userService;
    /**
     *
     * @param url 回调路径
     * @param appId
     * @param width
     * @param height
     * @param request
     * @return 微信登录中请求获取code的URL二维码
     */
    @Override
    public String getQrCodeByAuthorizationUrl(String url, String appId, int width, int height,
                                              HttpServletRequest request) {
        String getCodeURL = this.wxMpService.switchoverTo(appId).getOAuth2Service()
                .buildAuthorizationUrl(url,
                        WxConsts.OAuth2Scope.SNSAPI_USERINFO,request.getSession().getId()
                                +"&&forcePopup=true");
        System.out.println(getCodeURL);
        QrConfig config = new QrConfig(width , height);
        String qrCode = QrCodeUtil.generateAsBase64(getCodeURL,config,"png");
        log.info("[二维码] qrCode={}", qrCode.substring(0,qrCode.length() >> 2));
        return qrCode;
    }

    /**
     * 微信登录
     * @param wxOAuth2UserInfo
     * @param request
     * @return
     */
    @Override
    public LoginUserVO wxMpLogin(WxOAuth2UserInfo wxOAuth2UserInfo, HttpServletRequest request) {
        ThrowUtils.throwIf(wxOAuth2UserInfo == null || StrUtil.isBlank(wxOAuth2UserInfo.getOpenid()),
                ErrorCode.PARAMS_ERROR,"微信号信息错误");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mpOpenId", wxOAuth2UserInfo.getOpenid());
        User user = userService.getBaseMapper().selectOne(queryWrapper);
        LoginUserVO loginUserVO = userService.getLoginUserVO(user);
        // 用户不存在,即没有用户使用当前微信号登录
        if (user == null) {
            user = registerWxMpUser(wxOAuth2UserInfo,request);
            loginUserVO = userService.getLoginUserVO(user);
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        request.getSession().setMaxInactiveInterval(time);//设置用户登录时间

        LoginUser loginUser = userService.getLoginUserInfo(user, request);
        log.info("[当前登录用户] loginUser={}]", JSONUtil.toJsonStr(loginUser));
        OnlineUserManager.addUser(loginUser,request);//添加为已登录用户
        return loginUserVO;
    }

    /**
     * 绑定微信号
     * @param userId
     * @param wxOAuth2UserInfo
     * @param request
     * @return
     */
    @Override
    public Boolean bindWxMp(Long userId, WxOAuth2UserInfo wxOAuth2UserInfo, HttpServletRequest request) {
        ThrowUtils.throwIf(wxOAuth2UserInfo == null || StrUtil.isBlank(wxOAuth2UserInfo.getOpenid()),
                ErrorCode.PARAMS_ERROR,"微信号信息错误");
        //判断微信号是否已被绑定
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mpOpenId", wxOAuth2UserInfo.getOpenid());
        User user = userService.getBaseMapper().selectOne(queryWrapper);
        ThrowUtils.throwIf(user != null && StrUtil.isNotBlank(user.getId().toString()),
                ErrorCode.OPERATION_ERROR,"微信号已被绑定");

        //绑定微信号
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        user = userService.getBaseMapper().selectOne(queryWrapper);
        user.setMpOpenId(wxOAuth2UserInfo.getOpenid());
        boolean b = userService.updateById(user);
        return b;
    }

    /**
     * 解除绑定微信号
     * @param userId
     * @param request
     * @return
     */
    @Override
    public Boolean unbindWxMp(Long userId, HttpServletRequest request) {
        ThrowUtils.throwIf(userId == null || userId < 0 ,ErrorCode.PARAMS_ERROR,"用户id错误");

        //修改用户的微信mpOpenId
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        User user = userService.getBaseMapper().selectOne(queryWrapper);
        ThrowUtils.throwIf(StrUtil.isBlank(user.getMpOpenId()),ErrorCode.OPERATION_ERROR,"该用户还未绑定微信号");

        user.setMpOpenId("");
        boolean b = userService.updateById(user);
        return b;
    }

    private User registerWxMpUser(WxOAuth2UserInfo wxOAuth2UserInfo, HttpServletRequest request) {
        ThrowUtils.throwIf(wxOAuth2UserInfo == null || StrUtil.isBlank(wxOAuth2UserInfo.getOpenid()),
                ErrorCode.PARAMS_ERROR,"微信号信息错误");
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + 12345678).getBytes());
        User user = new User();
        user.setUserAccount(UUID.randomUUID().toString().replace("-", ""));
        user.setUserPassword(encryptPassword);
        user.setUserName(wxOAuth2UserInfo.getNickname());
        user.setMpOpenId(wxOAuth2UserInfo.getOpenid());
        user.setUserAvatar(wxOAuth2UserInfo.getHeadImgUrl());
        user.setUserProfile("这个人很神秘,什么都没有写");
        user.setUserRole(UserRoleEnum.USER.getValue());

        boolean save = userService.save(user);
        ThrowUtils.throwIf(!save,ErrorCode.OPERATION_ERROR,"微信用户注册失败");
        return user;
    }
}
