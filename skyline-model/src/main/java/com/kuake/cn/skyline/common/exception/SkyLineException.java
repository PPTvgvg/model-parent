package com.kuake.cn.skyline.common.exception;

/**
 * 描述：异常类
 * @author: kuake.cn
 * @create: 2021-03-25 16:21
 **/
public class SkyLineException extends Exception {
    public SkyLineException(String errorMessage) {
        super(errorMessage);
    }
    public SkyLineException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

}
