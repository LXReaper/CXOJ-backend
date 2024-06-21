package com.yp.CXOJ.config;

import com.binarywang.spring.starter.wxjava.mp.properties.WxMpProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 余炎培
 * @since 2024-05-29 星期三 21:17:23
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "wx.mp")
@Data
public class WxMpConfig {

    /**
     * 前端绑定微信号路径
     */
    private String bindUrl;

    /**
     * 微信登录后回调路径
     */
    private String url;

    /**
     * 微信公众平台的appId
     */
    private String appId;

    /**
     * 微信公众平台的appSecret
     */
    private String secret;

    /**
     * 微信公众平台的token
     */
    private String token;

    private WxMpProperties properties;
}
