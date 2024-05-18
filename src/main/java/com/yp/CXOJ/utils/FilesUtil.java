package com.yp.CXOJ.utils;

/**
 * @author 余炎培
 * @since 2024-05-16 星期四 22:52:14
 * 判断文件格式是否有效
 */
public class FilesUtil {
    // 图片允许的后缀扩展名
    public static String[] IMAGE_FILE_VALID = new String[] { ".png", ".bmp", ".jpg", ".jpeg",".pdf" };
    public static String[] VIDEO_FILE_VALID = new String[] { ".mp4", ".avi", ".mov", ".flv",".mkv" };

    /**
     * 图片格式校验
     * @param fileName
     * @return
     */
    public static boolean isImageFileAllowed(String fileName) {
        for (String ext : IMAGE_FILE_VALID) {
            if (ext.equals(fileName) || ext.toUpperCase().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 视频格式校验
     * @param fileName
     * @return
     */
    public static boolean isVideoFileAllowed(String fileName) {
        for (String ext : VIDEO_FILE_VALID) {
            if (ext.equals(fileName) || ext.toUpperCase().equals(fileName)) {
                return true;
            }
        }
        return false;
    }


}
