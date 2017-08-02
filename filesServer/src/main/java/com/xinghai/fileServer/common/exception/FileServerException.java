package com.xinghai.fileServer.common.exception;

/**
 * Created by scream on 2017/7/17.
 */
public class FileServerException extends BaseException {
    public FileServerException(FileServerErrorEnum errorCode){
        super(errorCode);
    }

    public FileServerException(String message ,FileServerErrorEnum code){
        super(message,code);
    }

    public FileServerException(String message,Throwable cause,FileServerErrorEnum errorCode){
        super(message,cause,errorCode);
    }

}
