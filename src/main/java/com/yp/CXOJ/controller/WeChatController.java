package com.yp.CXOJ.controller;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.yp.CXOJ.component.WxMpCodeEndPoint;
import cn.hutool.json.JSONUtil;
import com.yp.CXOJ.common.BaseResponse;
import com.yp.CXOJ.common.ResultUtils;
import com.yp.CXOJ.config.WxMpConfig;
import com.yp.CXOJ.exception.ThrowUtils;
import com.yp.CXOJ.model.vo.LoginUserVO;
import com.yp.CXOJ.service.WxMpLoginService;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * @author 余炎培
 * @since 2024-05-29 星期三 16:06:55
 */
@RestController
@RequestMapping("/weChat/mp")
@Slf4j
public class WeChatController {
    @Resource
    private WxMpLoginService wxMpLoginService;

    @Resource
    private WxMpService wxMpService;

    /**
     * 导入消息推送对象
     */
    @Resource
    private WxMpCodeEndPoint wxMpCodeEndPoint;
    /**
     * 微信公众平台的配置信息
     */
    @Resource
    private WxMpConfig wxMpConfig;

    /**
     *
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 随机字符串
     * @return 原样返回echostr参数内容(微信规定的)
     */
    @GetMapping("/wxCheck")
    public String wxCheck(
            @RequestParam(value = "signature") String signature,
            @RequestParam(value = "timestamp") String timestamp,
            @RequestParam(value = "nonce") String nonce,
            @RequestParam(value = "echostr") String echostr
    ) {
        log.info("check");
//        if (wxMpService.checkSignature(timestamp, nonce, signature)) {
//            return echostr;
//        } else {
//            return "";
//        }
        return echostr;
    }

    @GetMapping("/wxLogin")
    public void weChatLogin(HttpServletResponse httpServletResponse) {
        try {
            //重定向的后端路径
            String redirectURL = URLEncoder.encode(wxMpConfig.getUrl() + "/wxCallBack", "UTF-8");
            //构造二维码链接的地址
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfig.getAppId()
                    + "&redirect_uri=" + redirectURL +
                    "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

            //生成二维码,接收格式为png的图片
            QrCodeUtil.generate(url, 400, 400, "jpg", httpServletResponse.getOutputStream());

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    @GetMapping("/getAuthorizationUrl")
    @ApiOperation("获取授权URL")
    public BaseResponse<String> getAuthorizationUrl(HttpServletRequest request) {
        log.info("回调前的" + request.getHeader("User-Agent"));
        return ResultUtils.success(wxMpLoginService.getQrCodeByAuthorizationUrl(wxMpConfig.getUrl(),
                wxMpConfig.getAppId(),300,300,request));
    }

    @SneakyThrows(Exception.class)//检查异常并包装成非受检查异常抛出
    @GetMapping("/callback")
    @ApiOperation("回调-授权登录同意")
    public void callback(String code,String state,HttpServletRequest request,HttpServletResponse response) {
//        WxOAuth2Service oAuth2Service = wxMpService.switchoverTo(wxMpConfig.getAppId()).getOAuth2Service();
        wxMpCodeEndPoint.sendToAllClient(code,state);// todo 这里将code这个消息发送给前端，下面的代码再做一个接口，前端使用code去请求这个新的接口
//        WxOAuth2AccessToken wxOAuth2AccessToken = wxMpService.getOAuth2Service().getAccessToken(code);
//        WxOAuth2UserInfo wxOAuth2UserInfo = wxMpService.getOAuth2Service().getUserInfo(wxOAuth2AccessToken, "zh_CN");//如果zh_CN位置填写为null默认指定为zh_CN
//
//        log.info("[微信公众号] 授权回调 用户信息：[{}]", JSONUtil.toJsonStr(wxOAuth2UserInfo));
//
//        //微信登录的用户信息
//        LoginUserVO loginUserVO = wxMpLoginService.wxMpLogin(wxOAuth2UserInfo, request);
//        log.info("回调后的" + request.getHeader("User-Agent"));
//        return ResultUtils.success(loginUserVO);
    }

    /**
     * 使用微信服务发送的code换取微信用户token,接着拿着token换取微信用户信息
     * 接着写入数据库，并完成登录
     * @param code
     * @param request
     * @param response
     * @return
     */
    @SneakyThrows(Exception.class)//检查异常并包装成非受检查异常抛出
    @GetMapping("/wxMpLogin")
    @ApiOperation("获取微信用户信息")
    public BaseResponse<LoginUserVO> wxMpLogin(String code,HttpServletRequest request, HttpServletResponse response) {
        WxOAuth2AccessToken wxOAuth2AccessToken = wxMpService.getOAuth2Service().getAccessToken(code);
        WxOAuth2UserInfo wxOAuth2UserInfo = wxMpService.getOAuth2Service().getUserInfo(wxOAuth2AccessToken, "zh_CN");//如果zh_CN位置填写为null默认指定为zh_CN
        log.info("[微信公众号] 授权回调 用户信息：[{}]", JSONUtil.toJsonStr(wxOAuth2UserInfo));

        //微信登录的用户信息
        LoginUserVO loginUserVO = wxMpLoginService.wxMpLogin(wxOAuth2UserInfo, request);
        log.info("回调后的" + request.getHeader("User-Agent"));
        return ResultUtils.success(loginUserVO);
    }

    @SneakyThrows(Exception.class)
    @PostMapping("/bindWxMp")
    @ApiOperation("绑定微信号")
    public BaseResponse<Boolean> bindWxMp(Long userId,String code, HttpServletRequest request) {
        WxOAuth2AccessToken wxOAuth2AccessToken = wxMpService.getOAuth2Service().getAccessToken(code);
        WxOAuth2UserInfo wxOAuth2UserInfo = wxMpService.getOAuth2Service().getUserInfo(wxOAuth2AccessToken, "zh_CN");//如果zh_CN位置填写为null默认指定为zh_CN
        log.info("[微信公众号] 授权回调 用户信息：[{}]", JSONUtil.toJsonStr(wxOAuth2UserInfo));

        return ResultUtils.success(wxMpLoginService.bindWxMp(userId,wxOAuth2UserInfo,request));
    }

    @SneakyThrows(Exception.class)
    @PostMapping("/unbindWxMp")
    @ApiOperation("解除绑定微信号")
    public BaseResponse<Boolean> unbindWxMp(Long userId,HttpServletRequest request) {
        return ResultUtils.success(wxMpLoginService.unbindWxMp(userId,request));
    }
}
