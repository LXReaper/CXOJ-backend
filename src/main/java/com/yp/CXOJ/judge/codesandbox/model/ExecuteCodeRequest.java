package com.yp.CXOJ.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor //生成无参的构造方法
@AllArgsConstructor //生成所有参数都有的构造方法
public class ExecuteCodeRequest {
    /**
     * 输入用例
     */
    private List<String> inputList;
    /**
     * 输入代码
     */
    private String code;
    /**
     * 编程语言
     */
    private String language;
}
