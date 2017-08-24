package com.xinghai.fileServer.domain.InterfaceParameter.cassandra;

/**
 * Created by scream on 2017/8/3.
 */
public class GetFileData {
    private Integer id;
    private String fileId;
    private int blockCount;
    private Long size;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public int getBlockCount() {
        return blockCount;
    }

    public void setBlockCount(int blockCount) {
        this.blockCount = blockCount;
    }
}
