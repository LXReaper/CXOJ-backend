package com.yp.CXOJ.model.dto.file;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 余炎培
 * @since 2024-05-17 星期五 22:24:00
 */
@Data
public class FileUploadRequest {
    /**
     * 文件类型（如图片视频）
     */
    private String fileType;

    /**
     * 上传文件前缀(存放到七牛云的路径)
     */
    private String filePrefix;
}
