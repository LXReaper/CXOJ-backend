package com.yp.CXOJ.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公告类型枚举
 */
@Getter
public enum AnnouncementTypeStatusEnum {

    GAME("比赛","比赛公告"),
    COURSE("课程","课程公告"),
    TASK("任务","任务公告"),
    ACTIVITY("活动","活动公告"),
    BAN("违规","违规公告");


    private final String text;

    private final String value;

    AnnouncementTypeStatusEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static AnnouncementTypeStatusEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (AnnouncementTypeStatusEnum anEnum : AnnouncementTypeStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

}
