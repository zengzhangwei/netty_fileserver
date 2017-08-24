package com.xinghai.fileServer.fileHandle.fileContentService;

import com.xinghai.fileServer.domain.InterfaceParameter.cassandra.CreateFile;
import com.xinghai.fileServer.domain.InterfaceParameter.cassandra.DeleteFile;
import com.xinghai.fileServer.domain.InterfaceParameter.cassandra.GetFileData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * Created by scream on 2017/7/26.
 * 选择相关的存储方式
 */
public interface HandleFile {
    /**
     * @param createFile 存储需要的参数对象
     * @param data
     *  这是基于netty的一个存储数据接口
     */
    void saveFile(CreateFile createFile, InterfaceHttpData data);

    /**
     * 这是直接通过将文件数据放进数组，该接口不支持大文件的存储
     * @param createFile 存储需要的参数对象
     * @param data 文件内容
     */
    void saveFile(CreateFile createFile, byte[] data);


    /**
     * @param createFile 保存文件参数对象
     * @return 保存文件流
     */
     OutputStream saveFileByStream(CreateFile createFile);

    /**
     * 该接口通过将文件内容放进数组，不支持大文件的获取
     * @param file 获取文件的参数对象
     * @return 文件内容
     */
    byte[] getFileData(GetFileData file);

    /**
     * 该接口通过将文件从cassandra中取出来放进一个文件中
     * @param file 获取文件
     * @param raf 文件放置，这个对象应由调用者创建和释放
     */
    void getFileData(GetFileData file, RandomAccessFile raf);

    /**
     * @param file 参数对象
     */
    InputStream getFileDataByStream(GetFileData file);

    /**
     * 删除文件
     * @param file 删除文件需要的参数对象
     */
    void deleteFile(DeleteFile file);
}
