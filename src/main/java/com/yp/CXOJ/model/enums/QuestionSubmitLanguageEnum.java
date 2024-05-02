package com.yp.CXOJ.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目提交编程语言枚举
 */
@Getter
public enum QuestionSubmitLanguageEnum {

    JAVA("java", "java"),
    C("c", "c"),
    CPLUSPLUS("cpp", "cpp"),
    GOLANG("go", "go"),
    JAVASCRIPT("javascript", "javascript"),
    PYTHON("python", "python"),
    PASCAL("pascal", "pascal"),
    TYPESCRIPT("typescript", "typescript");

    private final String text;

    private final String value;

    QuestionSubmitLanguageEnum(String text, String value) {
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
     * 或者判断是否有该语言
     * @param value 编程语言
     * @return 有就返回该编程语言的枚举值，没有就返回null
     */
    public static QuestionSubmitLanguageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QuestionSubmitLanguageEnum anEnum : QuestionSubmitLanguageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

}
