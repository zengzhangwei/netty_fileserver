package com.xinghai.fileServer.domain.BO;

import java.util.UUID;

/**
 * Created by scream on 2017/7/26.
 */
public class CassandraFileBO {
    private UUID fileId;
    private int blockConut;
    private String content;

    public UUID getChunkId() {
        return fileId;
    }

    public void setChunkId(UUID chunkId) {
        this.fileId = chunkId;
    }

    public int getBlockConut() {
        return blockConut;
    }

    public void setBlockConut(int blockConut) {
        this.blockConut = blockConut;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
