package com.yp.CXOJ.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.yp.CXOJ.common.ErrorCode;
import com.yp.CXOJ.constant.File.FilesConstant;
import com.yp.CXOJ.exception.ThrowUtils;
import com.yp.CXOJ.service.QiNiuService;
import com.yp.CXOJ.utils.FilesUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 余炎培
 * @since 2024-05-17 星期五 16:17:40
 */
@Service
public class QiNiuServiceImpl implements QiNiuService {

    private static final Logger log = LoggerFactory.getLogger(QiNiuServiceImpl.class);
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
     *
     * @param multipartFile 要上传的文件
     * @param fileType      文件的类型（image,video,file）
     * @param filePrefix    存放到七牛云中的文件夹路径
     * @return
     */
    @Override
    public String uploadQiNiu(MultipartFile multipartFile, String fileType, String filePrefix) {
        //拿到上传文件原始名称（即上传时指定的名称）（getName方法得到的不是原来文件的名称）
        String originalFilename = multipartFile.getOriginalFilename();
        ThrowUtils.throwIf(StringUtils.isAnyBlank(originalFilename), ErrorCode.PARAMS_ERROR, "文件名称无效");

        //判断类型格式是否有效
        int idx = originalFilename.lastIndexOf(".");
        ThrowUtils.throwIf(idx == -1, ErrorCode.PARAMS_ERROR, "文件格式未知");
        String suffix = originalFilename.substring(idx);
        ThrowUtils.throwIf(fileType.equals(FilesConstant.IMAGE) && !FilesUtil.isImageFileAllowed(suffix),
                ErrorCode.PARAMS_ERROR, "图片格式错误");
        ThrowUtils.throwIf(fileType.equals(FilesConstant.VIDEO) && !FilesUtil.isVideoFileAllowed(suffix),
                ErrorCode.PARAMS_ERROR, "视频格式错误");

        /**
         * 上传七牛云服务器的操作
         */
        //Region指定数据存储区域，autoRegion()自动根据七牛云账号找到选的区域
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //实际处理上传操作的类
        UploadManager uploadManager = new UploadManager(cfg);

        //文件的存储名
        String key = filePrefix +
                DigestUtils.md5DigestAsHex(("YYP" + originalFilename).getBytes()) +
                suffix;

        try {
            Auth auth = Auth.create(accessKey, secretKey);//创建凭证
            String upToken = auth.uploadToken(bucket); //上传凭证

            try {
                //put 方法
                Response response = uploadManager.put(multipartFile.getInputStream(), key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(response.bodyString());
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url + key;
    }

    /**
     * 删除七牛云服务器中的文件
     * @param fileName
     * @return
     */
    @Override
    public Boolean deleteFileOnQiNiu(String fileName) {
        //Region指定数据存储区域，autoRegion()自动根据七牛云账号找到选的区域
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本

        Auth auth = Auth.create(accessKey, secretKey);//创建凭证
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, fileName);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            log.error("删除失败" + ex.code());
            log.error("删除失败" + ex.response.toString());
            return false;
        }
        return true;
    }
}
