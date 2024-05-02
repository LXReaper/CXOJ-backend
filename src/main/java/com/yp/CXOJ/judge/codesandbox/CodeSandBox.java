package com.yp.CXOJ.judge.codesandbox;

import com.yp.CXOJ.judge.codesandbox.model.ExecuteCodeRequest;
import com.yp.CXOJ.judge.codesandbox.model.ExecuteCodeResponse;

public interface CodeSandBox {
    /**
     * 执行代码
     * @param executeCodeRequest 执行代码请求
     * @return 执行响应
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
