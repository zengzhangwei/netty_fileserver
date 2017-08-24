package com.xinghai.fileServer.common.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by scream on 2017/7/12.
 */
//数据存储方式
public enum StorageSourceEnum {
    CASSANDRA(1,"CASSANDRA"),
    HDFS(2,"HDFS"),
    LOCAL(3,"LOCAL");//将文件存到服务器上

    private Integer typeId ;
    private String typeName;
    private static Map<Integer,StorageSourceEnum> idToEnumMap;
    static {
        Map<Integer,StorageSourceEnum> map = new HashMap<>(StorageSourceEnum.values().length);
        for(StorageSourceEnum storageSourceEnum : StorageSourceEnum.values()){
            map.put(storageSourceEnum.getTypeId(),storageSourceEnum);
        }
        idToEnumMap = Collections.unmodifiableMap(map);
    }
    StorageSourceEnum(Integer typeId, String typeName){
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public static StorageSourceEnum get(Integer id){
        return idToEnumMap.get(id);
    }
}
