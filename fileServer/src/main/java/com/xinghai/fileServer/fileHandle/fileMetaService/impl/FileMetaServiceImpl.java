package com.xinghai.fileServer.fileHandle.fileMetaService.impl;

import com.xinghai.fileServer.common.constant.MetaTypeEnum;
import com.xinghai.fileServer.common.constant.OrderEnum;
import com.xinghai.fileServer.common.constant.OrderFieldEnum;
import com.xinghai.fileServer.common.exception.FileServerErrorEnum;
import com.xinghai.fileServer.common.exception.FileServerException;
import com.xinghai.fileServer.common.util.MybatisUtils;
import com.xinghai.fileServer.dao.mysqlDao.FileMetaDao;
import com.xinghai.fileServer.domain.PO.FileMeta;
import com.xinghai.fileServer.domain.InterfaceParameter.fileMeta.*;
import com.xinghai.fileServer.fileHandle.fileMetaService.FileMetaService;
import com.xinghai.fileServer.config.FileServerConfig;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by scream on 2017/7/11.
 * 该类主要负责处理文件以及文件夹的管理
 */

public class FileMetaServiceImpl implements FileMetaService {
    /**
     * @param id 文件id
     * @return 文件信息
     */
    @Override
    public FileMeta getFileMetaById(Integer id) {
        try (final SqlSession session = MybatisUtils.openSession(false)) {
            final FileMetaDao mapper = session.getMapper(FileMetaDao.class);
            FileMeta fileMeta = mapper.getFileMetaById(id);
            return fileMeta;
        }
    }

    /**
     * @param id 文件id
     * @return
     */
    @Override
    public String getFileNameById(Integer id) {
        try (final SqlSession session = MybatisUtils.openSession(false)) {
            final FileMetaDao mapper = session.getMapper(FileMetaDao.class);
             String name = mapper.getFileNameById(id);
             return name;
        }
    }

    /**
     * 创建文件夹的接口
     * @param createFolder 创建文件夹需要的参数对象
     * @return 影响行数
     */
    @Override
    public int createFolder(CreateFolder createFolder) {
        try (final SqlSession session = MybatisUtils.openSession(true)) {
            final FileMetaDao mapper = session.getMapper(FileMetaDao.class);
            FileMeta fileMeta = new FileMeta();
            try {
                BeanUtils.copyProperties(fileMeta, createFolder);
            } catch (IllegalAccessException|InvocationTargetException e) {
                FileServerConfig.logger.info(e.getMessage());
                e.printStackTrace();
                throw new FileServerException(FileServerErrorEnum.COPY_BEAN_PROPERTITIE_ERROR);
            }
            int result = mapper.createFile(fileMeta);
            return result;
        }
    }

    /**
     * 删除文件夹
     * @param id 文件夹id
     * @return 影响行数
     */
    @Override
    public int deleteFolder(Integer id) {
        try (final SqlSession session = MybatisUtils.openSession(true)) {
            final FileMetaDao mapper = session.getMapper(FileMetaDao.class);
            int result = mapper.deleteFileMetaById(id);
            return result;
        }
    }

    /**
     * 查询id对应文件还是文件夹
     * @param id 文件或者文件夹的标示
     * @return 返回文件还是文件夹，1是文件夹，2是文件
     */
    @Override
    public String getTypeById(Integer id) {
        try(final SqlSession sqlSession = MybatisUtils.openSession(false)){
            final FileMetaDao  mapper = sqlSession.getMapper(FileMetaDao.class);
            String type = mapper.getMetaTypeById(id);
            return type;
        }
    }

    /**
     *
     * @param id 文件夹id 文件夹id
     * @param orderFieldEnum 排序字段，默认按照名字排序
     * @param orderEnum 选择正序还是倒序排
     * @return
     */
    @Override
    public FilesAndFoldersUnderFolder getFilesAndFoldersUnderFolder(Integer id, OrderFieldEnum orderFieldEnum, OrderEnum orderEnum) {
        try(final SqlSession session = MybatisUtils.openSession(false)){
            final FileMetaDao mapper  = session.getMapper(FileMetaDao.class);
            FilesAndFoldersUnderFolder filesAndFoldersUnderFolder = new FilesAndFoldersUnderFolder();
            //获取该目录下所有文件
            ListFilesUnderFolder files = new ListFilesUnderFolder();
            files.setParentId(id);
            files.setType(MetaTypeEnum.FILE.getTypeId());
            files.setOrderField(orderFieldEnum == null? null:orderFieldEnum.getType());
            files.setOrderSequence(orderEnum == null ? null :orderEnum.getId());
            List<FileMeta> subFilesList = mapper.getFilesUnderFolder(files);
            //将文件列表赋值
            filesAndFoldersUnderFolder.setSubFilesList(subFilesList);
            //获取该目录下所有文件夹
            files.setType(MetaTypeEnum.FOLDER.getTypeId());
            List<FileMeta> subFoldersList = mapper.getFilesUnderFolder(files);
            //将文件夹列表赋给对象
            filesAndFoldersUnderFolder.setSubFoldersList(subFoldersList);
            return filesAndFoldersUnderFolder;
        }
    }

    /**
     * @param id 文件夹id
     * @param orderFieldEnum 排序字段，默认按照名字排序
     * @param orderEnum 选择正序还是倒序排
     * @return 返回文件夹下文件
     */
    @Override
    public List<FileMeta> getFilesUnderFolder(Integer id, OrderFieldEnum orderFieldEnum, OrderEnum orderEnum) {
        try(final SqlSession session = MybatisUtils.openSession(false)){
            final FileMetaDao mapper = session.getMapper(FileMetaDao.class);
            ListFilesUnderFolder files = new ListFilesUnderFolder();
            files.setParentId(id);
            files.setType(MetaTypeEnum.FILE.getTypeId());
            files.setOrderField(orderFieldEnum == null? null:orderFieldEnum.getType());
            files.setOrderSequence(orderEnum == null ? null :orderEnum.getId());
            List<FileMeta> filesList = mapper.getFilesUnderFolder(files);
            return filesList;
        }
    }

    @Override
    public List<FileMeta> getFolderUnderFolder(Integer id, OrderFieldEnum orderFieldEnum, OrderEnum orderEnum) {
        try(final SqlSession session = MybatisUtils.openSession(false)){
            final FileMetaDao mapper = session.getMapper(FileMetaDao.class);
            ListFilesUnderFolder folders = new ListFilesUnderFolder();
            folders.setParentId(id);
            folders.setType(MetaTypeEnum.FOLDER.getTypeId());
            folders.setOrderField(orderFieldEnum == null? null:orderFieldEnum.getType());
            folders.setOrderSequence(orderEnum == null ? null :orderEnum.getId());
            List<FileMeta> foldersList = mapper.getFilesUnderFolder(folders);
            return foldersList;
        }
    }

    @Override
    public int updateName(Integer id,String name) {
        if(id == null){
            throw new FileServerException(FileServerErrorEnum.FILE_NOT_EXIST);
        }
        try(final SqlSession session = MybatisUtils.openSession(true)){
            final FileMetaDao fileMetaDao = session.getMapper(FileMetaDao.class);
            int result = fileMetaDao.updataNameById(id,name);
            return result;
        }
    }

    @Override
    public int updateParentId(Integer id,Integer parentId) {
        if(id == null || parentId == null){
            FileServerConfig.logger.info("移动文件时文件id或者目标文件夹id不能为空");
            throw new FileServerException(FileServerErrorEnum.FILE_NOT_EXIST);
        }
        try(final SqlSession session = MybatisUtils.openSession(true)){
            final FileMetaDao fileMetaDao = session.getMapper(FileMetaDao.class);
            int result = fileMetaDao.updateParentId(id,parentId);
            return result;
        }
    }
}
