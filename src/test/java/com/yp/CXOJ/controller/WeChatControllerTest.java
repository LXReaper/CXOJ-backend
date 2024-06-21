package com.yp.CXOJ.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.yp.CXOJ.config.WxMpConfig;
import me.chanjar.weixin.mp.api.WxMpService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.*;

class WeChatControllerTest {
    @Test
    public void weChatLogin() {
        try {
            //重定向的后端路径
            String redirectURL = URLEncoder.encode("http://vm92zr14234.vicp.fun:21299/weChat/wxCheck", "UTF-8");
//            String redirectURL = URLEncoder.encode("http://vm92zr14234.vicp.fun:21299" + "/wxCallBack", "UTF-8");
            //构造二维码链接的地址
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + "wx3e40bf8e5e3ec055"
                    + "&redirect_uri=" + redirectURL +
                    "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
            String url1 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2b5726143f1d3089&redirect_uri=http%3A%2F%2Fdevelopers.weixin.qq.com&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
            String url2 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2b5726143f1d3089&redirect_uri=http%3A%2F%2Fvm92zr14234.vicp.fun%3A21299%2Fapi%2FweChat%2Fmp%2Fcallback&response_type=code&scope=snsapi_userinfo&state=&connect_redirect=1#wechat_redirect";
            //生成二维码,接收格式为png的图片
            QrCodeUtil.generate(url2, 400, 400 , FileUtil.file("D:\\ai\\a.jpg"));

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}