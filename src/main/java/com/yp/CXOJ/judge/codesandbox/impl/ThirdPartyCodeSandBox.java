package com.yp.CXOJ.judge.codesandbox.impl;

import com.yp.CXOJ.judge.codesandbox.CodeSandBox;
import com.yp.CXOJ.judge.codesandbox.model.ExecuteCodeRequest;
import com.yp.CXOJ.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用非自己开发的沙箱）
 */
public class ThirdPartyCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return null;
    }
}
