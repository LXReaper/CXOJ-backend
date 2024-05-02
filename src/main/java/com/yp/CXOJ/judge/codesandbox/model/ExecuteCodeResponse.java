package com.yp.CXOJ.judge.codesandbox.model;

import com.yp.CXOJ.model.dto.questionsubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor //生成无参的构造方法
@AllArgsConstructor //生成所有参数都有的构造方法
public class ExecuteCodeResponse {
    /**
     * 输出用例
     */
    private List<String> outputList;
    /**
     * 执行信息
     */
    private String message;
    /**
     * 执行状态
     */
    private Integer status;
    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;
}
