package com.xinghai.fileServer.domain.InterfaceParameter.cassandra;

/**
 * Created by scream on 2017/8/3.
 */
public class DeleteFile {
    private Integer id;
    private String fileId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
