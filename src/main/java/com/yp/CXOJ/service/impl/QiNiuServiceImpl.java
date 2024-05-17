package com.yp.CXOJ.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.yp.CXOJ.common.ErrorCode;
import com.yp.CXOJ.exception.ThrowUtils;
import com.yp.CXOJ.service.QiNiuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 余炎培
 * @since 2024-05-17 星期五 16:17:40
 */
@Service
public class QiNiuServiceImpl implements QiNiuService {

    @Value("${oss.accessKey}")
    private String accessKey;

    @Value("${oss.secretKey}")
    private String secretKey;

    @Value("${oss.bucket}")
    private String bucket;

    @Value("${oss.url}")
    private String url;

    /**
     * 将文件上传到七牛云服务器
     * @param multipartFile
     * @param fileType
     * @return
     */
    @Override
    public Boolean uploadQiNiu(MultipartFile multipartFile, String fileType) {
        //拿到上传文件名称
        String originalFilename = multipartFile.getOriginalFilename();
        ThrowUtils.throwIf(StringUtils.isAnyBlank(originalFilename), ErrorCode.PARAMS_ERROR, "文件名称无效");

        /**
         * 上传七牛云服务器的操作
         */
        //Region指定数据存储区域，autoRegion()自动根据七牛云账号找到选的区域
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //实际处理上传操作的类
        UploadManager uploadManager = new UploadManager(cfg);

        //文件的存储名
        String key = multipartFile.getName();

        try {
            Auth auth = Auth.create(accessKey, secretKey);//创建凭证
            String upToken = auth.uploadToken(bucket); //上传凭证

            try{
                //put 方法
                Response response = uploadManager.put(multipartFile.getInputStream(), key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(response.bodyString());
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            }catch (QiniuException ex){
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        String newUrl = url + key;

        return null;
    }
}
