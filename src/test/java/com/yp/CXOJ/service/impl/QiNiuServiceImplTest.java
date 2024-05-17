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
import com.yp.CXOJ.constant.File.FilesConstant;
import com.yp.CXOJ.exception.ThrowUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

class QiNiuServiceImplTest {

    private String accessKey = "3I29nKtZ6RJs7h97YTwRlnjqT2ypz-L0PxiI-u_R";

    private String secretKey = "hcU5zOpdA5TQBf2p5eo_bOf_GZVUqZPk3QgtFQiz";

    private String bucket = "cxoj-files";

    MultipartFile multipartFile = new MockMultipartFile("image.jpg", "image.jpg", "image/jpeg", new byte[]{});
    String fileType = FilesConstant.IMAGE;
    @Test
    public void uploadQiNiu() {
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
            //测试用的文件
            InputStream inputStream = Files.newInputStream(Paths.get("D:\\ai\\640.jpg"));
            Auth auth = Auth.create(accessKey, secretKey);//创建凭证
            String upToken = auth.uploadToken(bucket); //上传凭证
            try{
                System.out.println(accessKey);//3I29nKtZ6RJs7h97YTwRlnjqT2ypz-L0PxiI-u_R
                //put 方法
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(response.bodyString());//{"hash":"Fv3O5bNG-6s_hTAbDYBKXnkQuMJ1","key":"image.jpg"}
                System.out.println(putRet.key);//image.jpg
                System.out.println(putRet.hash);//Fv3O5bNG-6s_hTAbDYBKXnkQuMJ1
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
    }
}