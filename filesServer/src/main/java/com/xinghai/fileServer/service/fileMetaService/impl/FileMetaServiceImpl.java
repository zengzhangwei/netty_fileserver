package com.xinghai.fileServer.service.fileMetaService.impl;

import com.xinghai.fileServer.common.util.MybatisUtils;
import com.xinghai.fileServer.dao.mysqlDao.FileMetaDao;
import com.xinghai.fileServer.domain.BO.FileMetaBO;
import com.xinghai.fileServer.service.fileMetaService.FileMetaService;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by scream on 2017/7/11.
 */

public class FileMetaServiceImpl implements FileMetaService {

    //获取所有文件的meta
    @Override
    public List<FileMetaBO> ListAllFileMeta() {
        try (final SqlSession session = MybatisUtils.openSession(false)) {
            final FileMetaDao mapper = session.getMapper(FileMetaDao.class);
            List<FileMetaBO> listFileMetaBO = mapper.getAllFileMeta();
            return listFileMetaBO;
        }
    }

    @Override
    public FileMetaBO getFileMetaById(Integer id) {
        try (final SqlSession session = MybatisUtils.openSession(false)) {
            final FileMetaDao mapper = session.getMapper(FileMetaDao.class);
            FileMetaBO fileMetaBO = mapper.getFileMetaById(id);
            return fileMetaBO;
        }
    }

    @Override
    public int createFile(FileMetaBO file) {
        try(final SqlSession session = MybatisUtils.openSession(true)){
            final FileMetaDao mapper = session.getMapper(FileMetaDao.class);
            return  mapper.createFile(file);
        }
    }

    @Override
    public FileMetaBO getFileNameById(Integer id) {
       return null;
    }

    @Override
    public int deleteFileMetaById(Integer id) {
        try(final SqlSession session = MybatisUtils.openSession(true)){
         final FileMetaDao mapper = session.getMapper(FileMetaDao.class);
         return mapper.deleteFileMetaById(id);
        }
    }
}
