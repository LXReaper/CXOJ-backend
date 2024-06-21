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
     * @param filePrefix
     * @return 文件的远程URL
     */
    String uploadQiNiu(MultipartFile multipartFile, String fileType,String filePrefix);

    /**
     * 删除七牛云服务器中的文件
     * @param fileName
     * @return
     */
    Boolean deleteFileOnQiNiu(String fileName);
}
