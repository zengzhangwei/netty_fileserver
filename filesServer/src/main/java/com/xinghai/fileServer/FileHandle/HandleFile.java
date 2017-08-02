package com.xinghai.fileServer.FileHandle;

import com.xinghai.fileServer.domain.BO.FileMetaBO;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.RandomAccessFile;

/**
 * Created by scream on 2017/7/26.
 * 选择相关的存储方式
 */
public interface HandleFile {
    void saveFile(FileMetaBO fileMetaBO, InterfaceHttpData data);
    byte[] getFile(FileMetaBO file);
    void getFile(FileMetaBO file, RandomAccessFile raf);
    void deleteFile(FileMetaBO file);
}
