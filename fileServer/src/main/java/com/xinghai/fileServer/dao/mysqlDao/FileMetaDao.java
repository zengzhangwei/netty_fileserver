package com.xinghai.fileServer.dao.mysqlDao;

import com.xinghai.fileServer.domain.PO.FileMeta;
import com.xinghai.fileServer.domain.InterfaceParameter.fileMeta.ListFilesUnderFolder;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by scream on 2017/7/11.
 */
@Mapper
public interface FileMetaDao {
    @Select("Select * from file_meta where id = #{id}")
    FileMeta getFileMetaById(Integer id);

    @Insert("insert into file_meta(id,parent_id,file_id,block_count,created_by,created_on,modified_by,modified_on,name,size,content_type,hash,type,storage_source) " +
            "values(#{f.id},#{f.parentId},#{f.fileId},#{f.blockCount},#{f.createdBy},#{f.createdOn},#{f.modifiedBy},#{f.modifiedOn},#{f.name},#{f.size},#{f.contentType},#{f.hash},#{f.type},#{f.storageSource})")
    int createFile(@Param("f") FileMeta file);

    @Select("select name from file_meta where id = #{id}")
    String getFileNameById(Integer id);

    @Delete("delete from file_meta where id = #{id}")
    int deleteFileMetaById(Integer id);

    @Select("select type from file_meta where id = #{id}")
    String getMetaTypeById(Integer id);


    @SelectProvider(type = FileMetaProvider.class,method = "getFilesUnderFolder")
    List<FileMeta> getFilesUnderFolder(ListFilesUnderFolder listFilesUnderFolder);

    @Update("update file_meta set name = #{name} where id = #{id}")
    int updataNameById(@Param("id") Integer id, @Param("name") String name);

    @Update("update file_meta set parent_id = #{parentId} where id = #{id}")
    int updateParentId(@Param("id") Integer id ,@Param("parentId") Integer parentId);
}
