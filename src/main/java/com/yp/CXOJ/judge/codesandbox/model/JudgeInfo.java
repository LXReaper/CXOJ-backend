package com.yp.CXOJ.judge.codesandbox.model;

import lombok.Data;

/*
* 判题信息,比如AC或者超时
* */
@Data
public class JudgeInfo {
    /*
    * 程序执行信息
    * */
    private String message;

    /*
    * 消耗时间
    * */
    private Long time;

    /*
    * 消耗内存
    * */
    private Long memory;
}
