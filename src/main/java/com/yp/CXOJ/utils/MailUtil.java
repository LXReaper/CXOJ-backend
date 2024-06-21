package com.yp.CXOJ.utils;

/**
 * @author 余炎培
 * @since 2024-06-17 星期一 11:14:39
 */
public class MailUtil {
    //邮箱后缀
    private static String[] mailSuffix = new String[]{"@qq.com", "@gmail.com", "@163.com", "@sina.com", "@aliyun.com", "@hotmail.com", "@icloud.com","@yahoo.com"};

    //判断mail的后缀是否正确
    public static Boolean isMail(String mail) {
        for (String suffix : mailSuffix) {
            if (mail.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
}
