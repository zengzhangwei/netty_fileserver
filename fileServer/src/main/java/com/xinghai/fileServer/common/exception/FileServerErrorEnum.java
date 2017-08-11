package com.xinghai.fileServer.common.exception;

/**
 * Created by scream on 2017/7/17.
 */
public enum FileServerErrorEnum implements ErrorCode {
    UNKNOWN(1000,"未知错误"),
    FILE_NOT_EXIST(1001,"文件或者文件夹不存在"),
    METHOD_NOT_SUPPORT(1002,"方法不支持"),
    ID_NOT_FOUND(1003,"ID不存在"),
    UNKNOWN_HTTP_OBJECT(1004,"未知的http属性"),
    READ_FILE_CHUNK_ERROR(1005,"读取文件块错误"),
    INITIALIZER_PROCESSOR_ERROR(1006,"初始化处理程序出错"),
    REQUEST_PATH_NOT_EXIST(1007,"请求路径不存在"),
    PATH_CAN_NOT_MATCH_METHOD(1008,"请求路径和方法不能对应"),
    STORAGE_METHOD_NOT_SUPPORT(1009,"存储方式不支持"),
    INSERT_CHUNK_ERROR(1010,"插入数据块出错"),
    UNSUPPORTED_ENCODING(1011,"不支持的编码"),
    UNRECEIVE_NO_DATA(1012,"未接收到任何待处理的数据"),
    FILE_TOO_LARGE(1013,"文件太大，不能处理"),
    FILE_META_INSERT_ERROR(1014,"插入文件信息时出错"),
    CREATE_RANDOM_FILE_ERROR(1015,"创建随机文件出错"),
    COPY_BEAN_PROPERTITIE_ERROR(1016,"拷贝对象属性错误"),
    CAN_NOT_GET_MORE_BLOCK(1017,"无法获取更多数据块");
    private int code;
    private String message;

    FileServerErrorEnum(int code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode(){
        return code;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
