package com.xinghai.fileServer.common.exception;

/**
 * Created by scream on 2017/7/17.
 */
public interface ErrorCode {
    /**
     * 错误码
     * @return
     */
    Integer getCode();

    /**
     * 错误信息
     * @return
     */
    String getMessage();
}
