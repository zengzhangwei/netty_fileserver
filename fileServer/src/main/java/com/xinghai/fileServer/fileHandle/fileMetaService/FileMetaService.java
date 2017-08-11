package com.xinghai.fileServer.fileHandle.fileMetaService;

import com.xinghai.fileServer.common.constant.OrderEnum;
import com.xinghai.fileServer.common.constant.OrderFieldEnum;
import com.xinghai.fileServer.domain.PO.FileMeta;
import com.xinghai.fileServer.domain.InterfaceParameter.fileMeta.*;

import java.util.List;

/**
 * Created by scream on 2017/7/11
 * 获取文件基本信息.
 */
public interface FileMetaService {
    /**
     * @param id 文件id
     * @return 文件信息
     */
    FileMeta getFileMetaById(Integer id);


    /**
     * @param id 文件id
     * @return
     */
    String getFileNameById(Integer id);

    /**
     * 创建文件夹的接口
     * @param createFolder 创建文件夹需要的参数对象
     * @return 影响行数
     */
    int createFolder(CreateFolder createFolder);

    /**
     * 删除文件夹
     * @param id 文件夹id
     * @return 影响行数
     */
    int deleteFolder(Integer id);

    /**
     * 查询id对应文件还是文件夹
     * @param id 文件或者文件夹的标示
     * @return 返回文件还是文件夹，1是文件夹，2是文件
     */
    String getTypeById(Integer id);


    /**
     *
     * @param id 文件夹id
     * @param orderFieldEnum 排序字段，默认按照名字排序
     * @param orderEnum 选择正序还是倒序排
     * @return 返回文件夹下文件和文件夹的集合
     */
    FilesAndFoldersUnderFolder getFilesAndFoldersUnderFolder(Integer id, OrderFieldEnum orderFieldEnum, OrderEnum orderEnum);

    /**
     *
     * @param id 文件夹id
     * @param orderFieldEnum 排序字段，默认按照名字排序
     * @param orderEnum 选择正序还是倒序排
     * @return 返回文件夹下文件
     */
    List<FileMeta> getFilesUnderFolder(Integer id, OrderFieldEnum orderFieldEnum, OrderEnum orderEnum );

    /**
     *
     * @param id 文件夹id
     * @param orderFieldEnum 排序字段，默认按照名字排序
     * @param orderEnum 选择正序还是倒序排
     * @return 返回文件夹下文件夹
     */
    List<FileMeta> getFolderUnderFolder(Integer id, OrderFieldEnum orderFieldEnum, OrderEnum orderEnum);

    /**
     *
     * @param id 文件或者文件夹id
     * @param name
     * @return
     */
    int updateName(Integer id,String name);


    /**
     * 移动文件（夹）
     * @param id
     * @param parentId
     * @return
     */
    int updateParentId(Integer id,Integer parentId);


}
