package com.xinghai.fileServer.domain.httpParameter;

/**
 * Created by scream on 2017/7/27.
 */
public class FileImportParameter {
   private int parentDirId;
   private String filePath;

    public int getParentDirId() {
        return parentDirId;
    }

    public void setParentDirId(int parentDirId) {
        this.parentDirId = parentDirId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
