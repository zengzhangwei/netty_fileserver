package com.xinghai.fileServer.config;


import java.util.List;

/**
 * Created by scream on 2017/8/10.
 */
public class ConfigurationMatchJSON {

    private List cassandra;//cassandra集群ip
    private String hostIp;//文件服务器ip
    private int hostPort;//文件服务端口
    private String fileDir;//文件存储路径
    private int httpCacheSeconds;//缓存时间
    private int chunkSize;//分块大小，1m
    private int sizeInMemory;//上传文件时文件放置内存大小，其余存盘
    private int getDataLengthEachTime;//分块时一次取多少放进内存
    private long maxFileLengthInMemory;//内存最大放置文件大小
    private int tikaDetectSize;//检测文件时提取文件大小
    private String tempFileDir;//临时文件存放路径
    private int bizThreadSize;//线程大小

    public List getCassandra() {
        return cassandra;
    }

    public void setCassandra(List cassandra) {
        this.cassandra = cassandra;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public int getHttpCacheSeconds() {
        return httpCacheSeconds;
    }

    public void setHttpCacheSeconds(int httpCacheSeconds) {
        this.httpCacheSeconds = httpCacheSeconds;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public int getSizeInMemory() {
        return sizeInMemory;
    }

    public void setSizeInMemory(int sizeInMemory) {
        this.sizeInMemory = sizeInMemory;
    }

    public int getGetDataLengthEachTime() {
        return getDataLengthEachTime;
    }

    public void setGetDataLengthEachTime(int getDataLengthEachTime) {
        this.getDataLengthEachTime = getDataLengthEachTime;
    }

    public long getMaxFileLengthInMemory() {
        return maxFileLengthInMemory;
    }

    public void setMaxFileLengthInMemory(long maxFileLengthInMemory) {
        this.maxFileLengthInMemory = maxFileLengthInMemory;
    }

    public int getTikaDetectSize() {
        return tikaDetectSize;
    }

    public void setTikaDetectSize(int tikaDetectSize) {
        this.tikaDetectSize = tikaDetectSize;
    }

    public String getTempFileDir() {
        return tempFileDir;
    }

    public void setTempFileDir(String tempFileDir) {
        this.tempFileDir = tempFileDir;
    }

    public int getBizThreadSize() {
        return bizThreadSize;
    }

    public void setBizThreadSize(int bizThreadSize) {
        this.bizThreadSize = bizThreadSize;
    }
}
