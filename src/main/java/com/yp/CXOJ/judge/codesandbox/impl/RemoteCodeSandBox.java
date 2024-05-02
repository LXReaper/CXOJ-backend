package com.yp.CXOJ.judge.codesandbox.impl;

import com.yp.CXOJ.judge.codesandbox.CodeSandBox;
import com.yp.CXOJ.judge.codesandbox.model.ExecuteCodeRequest;
import com.yp.CXOJ.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 远程代码沙箱（实际调用接口的沙箱）
 */
public class RemoteCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return null;
    }
}
