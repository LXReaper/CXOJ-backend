package com.yp.CXOJ.judge.codesandbox;

import com.yp.CXOJ.judge.codesandbox.impl.ExampleCodeSandBox;
import com.yp.CXOJ.judge.codesandbox.impl.RemoteCodeSandBox;
import com.yp.CXOJ.judge.codesandbox.impl.ThirdPartyCodeSandBox;

/**
 * 代码沙箱创建工厂
 */
public class CodeSandBoxFactory {
    /**
     * 创建代码沙箱实例
     * @param type 沙箱类型
     * @return
     */
    public static CodeSandBox newInstance(String type){
        switch (type){
            case "example" :
                return new ExampleCodeSandBox();
            case "remote" :
                return new RemoteCodeSandBox();
            case "thirdParty" :
                return new ThirdPartyCodeSandBox();
            default :
                return new ExampleCodeSandBox();
        }
    }
}
