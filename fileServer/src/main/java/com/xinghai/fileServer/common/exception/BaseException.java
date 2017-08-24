package com.xinghai.fileServer.common.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by scream on 2017/7/17.
 */
public abstract class BaseException extends RuntimeException implements ErrorCode{
    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }

    public BaseException(String message,ErrorCode errorCode){
        super(message);
        this.errorCode =errorCode;
    }

    public BaseException(String message,Throwable cause,ErrorCode errorCode){
        super(message,cause);
        this.errorCode = errorCode;
    }

    @Override
    public Integer getCode(){
       return errorCode != null?errorCode.getCode():null;
    }

    @Override
    public String getMessage(){
       return StringUtils.isBlank(super.getMessage())&&errorCode !=null?errorCode.getMessage():super.getMessage();
    }

    public ErrorCode getErrorCode(){
        return errorCode;
    }
}
