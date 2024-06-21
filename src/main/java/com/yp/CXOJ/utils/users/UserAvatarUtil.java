package com.yp.CXOJ.utils.users;

import com.yp.CXOJ.model.entity.User;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 余炎培
 * @since 2024-05-30 星期四 13:29:46
 * 用户头像工具
 */
public class UserAvatarUtil {
    /**
     * 判断旧头像是否合法（是否为空或者为默认头像）、新头像是否合法（即是否有头像要更新）
     * @param newUser 新用户信息
     * @param oldUser 旧用户信息
     * @return 是或否
     */
    public static boolean isValidAvatarData(User newUser, User oldUser) {
        if (StringUtils.isNotBlank(newUser.getUserAvatar()) &&
                StringUtils.isNotBlank(oldUser.getUserAvatar()) &&
                !oldUser.getUserAvatar().equals("https://img.zcool.cn/community/01460b57e4a6fa0000012e7ed75e83.png?x-oss-process=image/auto-orient,1/resize,m_lfit,w_1280,limit_1/sharpen,100")) {
            return true;
        }
        return false;
    }

    /**
     * 新用户数据的头像是否有效（新用户数据的头像是否为空，且头像是否为默认头像）
     * @param newUser 新用户数据
     * @return 是或否
     */
    public static boolean isValidAvatarData(User newUser) {
        if (StringUtils.isNotBlank(newUser.getUserAvatar()) &&
                !newUser.getUserAvatar().equals("https://img.zcool.cn/community/01460b57e4a6fa0000012e7ed75e83.png?x-oss-process=image/auto-orient,1/resize,m_lfit,w_1280,limit_1/sharpen,100")) {
            return true;
        }
        return false;
    }
}
