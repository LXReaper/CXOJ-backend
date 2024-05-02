package com.yp.CXOJ.judge.codesandbox;


import com.yp.CXOJ.judge.codesandbox.model.ExecuteCodeRequest;
import com.yp.CXOJ.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 增强原有的代码沙箱 , 方便编写日志
 */
@Slf4j
@AllArgsConstructor //构造方法
public class CodeSandBoxProxy implements CodeSandBox {

    private final CodeSandBox codeSandBox;
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息：" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        log.info("代码沙箱的响应信息：" + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
