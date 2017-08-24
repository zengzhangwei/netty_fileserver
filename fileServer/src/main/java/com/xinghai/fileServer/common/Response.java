package com.xinghai.fileServer.common;

/**
 * Created by scream on 2017/7/12.
 */
public class Response<T> {
    private int errno = 0;
    private String errmsg = "success";
    private T data;
    public Response(){
    }
    public Response(int errno,String errmsg){
        this.errno = errno;
        this.errmsg = errmsg;
    }

    public Response(int errno,String errmsg,T data){
        this.errno = errno;
        this.errmsg = errmsg;
        this.data = data;
    }

    public int getErrno() {
        return errno;
    }
    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
