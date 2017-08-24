package com.xinghai.fileServer.dao.mysqlDao;

import com.xinghai.fileServer.domain.InterfaceParameter.fileMeta.ListFilesUnderFolder;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by scream on 2017/8/3.
 */
public class FileMetaProvider {
    public String getFilesUnderFolder(ListFilesUnderFolder listFilesUnderFolder){
        String sql = "select * from file_meta where parent_id = " + listFilesUnderFolder.getParentId() + " and type = '" + listFilesUnderFolder.getType() + "' ";
        if(StringUtils.isNoneBlank(listFilesUnderFolder.getOrderField())){
            sql+=  " order by " + listFilesUnderFolder.getOrderField();
            if(listFilesUnderFolder.getOrderSequence() != null){
                sql += listFilesUnderFolder.getOrderSequence().equals(2) ? " DESC" :" ASC";
            }
        }
        return  sql;
    }
}
