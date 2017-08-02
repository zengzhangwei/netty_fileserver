package com.xinghai.fileServer.dao.mysqlDao;

import com.xinghai.fileServer.domain.BO.FileMetaBO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by scream on 2017/7/11.
 */
@Mapper
public interface FileMetaDao {
    @Select("select * from file_meta")
    List<FileMetaBO> getAllFileMeta();

    @Select("Select * from file_meta where id = #{id}")
    FileMetaBO getFileMetaById(Integer id);

    @Insert("insert into file_meta(id,parent_id,file_id,block_count,created_by,created_on,modified_by,modified_on,name,size,content_type,hash,type,storage_source) " +
            "values(#{f.id},#{f.parentId},#{f.fileId},#{f.blockCount},#{f.createdBy},#{f.createdOn},#{f.modifiedBy},#{f.modifiedOn},#{f.name},#{f.size},#{f.contentType},#{f.hash},#{f.type},#{f.storageSource})")
    int createFile(@Param("f") FileMetaBO file);

    @Select("select name,created_on from file_meta where id = #{id}")
    @Results({@Result(property = "createdOn",column = "created_on",javaType = java.util.Date.class),
              @Result(property = "name",column = "name")})
    FileMetaBO getFileNameById(Integer id);

    @Delete("delete from file_meta where id = #{id}")
    int deleteFileMetaById(Integer id);

}
