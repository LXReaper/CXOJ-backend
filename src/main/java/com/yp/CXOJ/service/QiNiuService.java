package com.yp.CXOJ.service;

import org.springframework.web.multipart.MultipartFile;


/**
 * @author 余炎培
 * @since 2024-05-16 星期四 22:57:07
 */
public interface QiNiuService {
    /**
     * 上传到七牛云服务器
     * @param multipartFile
     * @param fileType
     * @return
     */
    Boolean uploadQiNiu(MultipartFile multipartFile, String fileType);
}
