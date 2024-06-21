package com.yp.CXOJ.model.dto.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* 题目配置
* */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JudgeConfig {
    /*
    * 时间限制(ms)
    * */
    private Long timeLimit;

    /*
    * 内存限制(MB)
    * */
    private Long memoryLimit;

    /*
    * 堆栈限制(MB)
    * */
    private Long stackLimit;
}
