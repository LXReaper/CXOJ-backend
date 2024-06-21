package com.yp.CXOJ.service;

import com.yp.CXOJ.model.vo.LoginUserVO;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 余炎培
 * @since 2024-06-08 星期六 12:24:30
 * 微信公众平台
 */
@Service
public interface WxMpLoginService {
    /**
     * 返回微信登录中请求获取code的URL二维码
     * @param url 回调路径
     * @param appId
     * @param width
     * @param height
     * @param request
     * @return 微信登录中请求获取code的URL二维码
     */
    String getQrCodeByAuthorizationUrl(String url,String appId,int width,int height,HttpServletRequest request);

    /**
     * 微信登录
     * @param wxOAuth2UserInfo
     * @param request
     * @return
     */
    LoginUserVO wxMpLogin(WxOAuth2UserInfo wxOAuth2UserInfo, HttpServletRequest request);

    /**
     * 绑定微信号
     * @param userId
     * @param wxOAuth2UserInfo
     * @param request
     * @return
     */
    Boolean bindWxMp(Long userId,WxOAuth2UserInfo wxOAuth2UserInfo, HttpServletRequest request);

    /**
     * 解除绑定微信号
     * @param userId
     * @param request
     * @return
     */
    Boolean unbindWxMp(Long userId ,HttpServletRequest request);
}
