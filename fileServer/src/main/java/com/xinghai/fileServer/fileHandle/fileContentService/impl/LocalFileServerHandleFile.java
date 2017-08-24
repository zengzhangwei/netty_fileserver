package com.xinghai.fileServer.fileHandle.fileContentService.impl;

import com.xinghai.fileServer.common.exception.FileServerErrorEnum;
import com.xinghai.fileServer.common.exception.FileServerException;
import com.xinghai.fileServer.config.FileServerConfig;
import com.xinghai.fileServer.domain.InterfaceParameter.cassandra.CreateFile;
import com.xinghai.fileServer.domain.InterfaceParameter.cassandra.DeleteFile;
import com.xinghai.fileServer.domain.InterfaceParameter.cassandra.GetFileData;
import com.xinghai.fileServer.domain.PO.FileMeta;
import com.xinghai.fileServer.fileHandle.fileContentService.HandleFile;
import com.xinghai.fileServer.fileHandle.fileMetaService.FileMetaService;
import com.xinghai.fileServer.fileHandle.fileMetaService.impl.FileMetaServiceImpl;
import com.xinghai.fileServer.fileHandle.fileUtil.CopyPropertityUtil;
import com.xinghai.fileServer.fileHandle.fileUtil.ManageFileMeta;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.apache.ibatis.jdbc.SQL;

import java.io.*;

/**
 * Created by scream on 2017/8/17.
 * 将文件存在服务器中指定文件夹下
 */
public class LocalFileServerHandleFile implements HandleFile {
    @Override
    public void saveFile(CreateFile createFile, InterfaceHttpData data) {
        FileMeta fileMeta = new FileMeta();
        //属性赋值
        CopyPropertityUtil.copyProperties(fileMeta, createFile);
        String filePath = FileServerConfig.FILE_DIR;
        if (null == filePath) {
            throw new FileServerException(FileServerErrorEnum.STORE_FIEL_DIRCTORY_NOT_EXIST);
        }
        filePath = filePath + File.separator + fileMeta.getName();
        File newFile = new File(filePath);
        if (newFile.exists()) {
            throw new FileServerException(FileServerErrorEnum.FILE_EXIST_ON_SERVER);
        }
        try {
            ((FileUpload) data).renameTo(newFile);
        } catch (IOException e) {
            FileServerConfig.logger.error("保存文件时出错：" + e.getMessage());
            throw new FileServerException(FileServerErrorEnum.STORE_FILE_ERROR);
        }
        //将文件信息存进mysql，存储失败时删除服务端文件，避免下次上传失败
        saveFileMetaIntoMysql(fileMeta, newFile);
    }

    @Override
    public void saveFile(CreateFile createFile, byte[] data) {
          //todo 以数组形式存文件到本地
    }

    @Override
    public OutputStream saveFileByStream(CreateFile createFile) {
        //todo 以流形式存文件到本地
        return null;
    }

    @Override
    public byte[] getFileData(GetFileData file) {
        //todo 以数组形式获取本地文件
        return new byte[0];
    }

    @Deprecated
    @Override
    public void getFileData(GetFileData file, RandomAccessFile raf) {
        //临时文件的方式获取文件，对于将文件存到本地该方法没有意义
    }

    @Override
    public InputStream getFileDataByStream(GetFileData file) {
        FileMetaService fileMetaService = new FileMetaServiceImpl();
        String name = fileMetaService.getFileNameById(file.getId());
        if (null != name) {
            File download = new File(FileServerConfig.FILE_DIR + File.separator + name);
            if (download.exists()) {
                try {
                    InputStream is = new FileInputStream(download);
                    return is;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    FileServerConfig.logger.error("read the file id is " + file.getId() + " and file name: " + name + " error");
                    throw new FileServerException(FileServerErrorEnum.FILE_NOT_EXIST);
                }
            }
        }
        return null;
    }

    @Override
    public void deleteFile(DeleteFile file) {
        FileMetaService fileMetaService = new FileMetaServiceImpl();
        String fileName = fileMetaService.getFileNameById(file.getId());
        //删除服务端文件
        if(null != fileName){
            File deleteFile = new File(FileServerConfig.FILE_DIR + File.separator + fileName);
            //boolean isDelete = deleteFile.delete();
            if(deleteFile.exists()){
                deleteFile.delete();
            }
        }
        //删除文件meta
        ManageFileMeta manageFileMeta = new ManageFileMeta();
        manageFileMeta.deleteFileMetaById(file.getId());
    }

    /**
     * @param fileMeta 文件信息
     * @param file     文件对象
     */
    private void saveFileMetaIntoMysql(FileMeta fileMeta, File file) {
        try {
            ManageFileMeta manageFileMeta = new ManageFileMeta();
            int result = manageFileMeta.createFile(fileMeta);
            FileServerConfig.logger.info("the result: " + result);
            if (result != 1) {
                FileServerConfig.logger.info("insert data in to mysql failed!");
            }
        } catch (Exception e) {
            FileServerConfig.logger.error("向mysql插入文件信息时出错： " + e.getMessage());
            //删除文件
            if (null != file && file.exists()) {
                file.delete();
            }
            throw new FileServerException(FileServerErrorEnum.FILE_META_INSERT_ERROR);
        }
    }


}
