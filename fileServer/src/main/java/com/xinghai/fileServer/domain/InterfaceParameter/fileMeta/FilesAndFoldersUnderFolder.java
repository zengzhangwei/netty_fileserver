package com.xinghai.fileServer.domain.InterfaceParameter.fileMeta;

import com.xinghai.fileServer.domain.PO.FileMeta;

import java.util.List;

/**
 * Created by scream on 2017/8/3.
 */
public class FilesAndFoldersUnderFolder {
    List<FileMeta> subFilesList;
    List<FileMeta> subFoldersList;

    public List<FileMeta> getSubFilesList() {
        return subFilesList;
    }

    public void setSubFilesList(List<FileMeta> subFilesList) {
        this.subFilesList = subFilesList;
    }

    public List<FileMeta> getSubFoldersList() {
        return subFoldersList;
    }

    public void setSubFoldersList(List<FileMeta> subFoldersList) {
        this.subFoldersList = subFoldersList;
    }
}
