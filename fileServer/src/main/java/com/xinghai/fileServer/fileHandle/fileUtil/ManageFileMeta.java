package com.xinghai.fileServer.fileHandle.fileUtil;

import com.xinghai.fileServer.common.util.MybatisUtils;
import com.xinghai.fileServer.dao.mysqlDao.FileMetaDao;
import com.xinghai.fileServer.domain.PO.FileMeta;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by scream on 2017/8/3.
 * 该类主要是供数据存取时文件信息的管理，不由业务层调用
 */
public class ManageFileMeta {
    /**
     *创建文件
     * @param file
     * @return
     */
    public int createFile(FileMeta file) {
        try(final SqlSession session = MybatisUtils.openSession(true)){
            final FileMetaDao mapper = session.getMapper(FileMetaDao.class);
            return  mapper.createFile(file);
        }
    }

    /**
     * @param id
     * @return
     */
    public int deleteFileMetaById(Integer id) {
        try(final SqlSession session = MybatisUtils.openSession(true)){
            final FileMetaDao mapper = session.getMapper(FileMetaDao.class);
            return mapper.deleteFileMetaById(id);
        }
    }
}
