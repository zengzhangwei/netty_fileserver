package com.xinghai.fileServer.service.fileMetaService;

import com.xinghai.fileServer.domain.BO.FileMetaBO;

import java.util.List;

/**
 * Created by scream on 2017/7/11
 * 获取文件基本信息.
 */
public interface FileMetaService {
    List<FileMetaBO> ListAllFileMeta();
    FileMetaBO getFileMetaById(Integer id);
    int createFile(FileMetaBO file);
    FileMetaBO getFileNameById(Integer id);
    int deleteFileMetaById(Integer id);
}
