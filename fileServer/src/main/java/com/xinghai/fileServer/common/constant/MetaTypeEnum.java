package com.xinghai.fileServer.common.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by scream on 2017/7/28.
 */
public enum MetaTypeEnum {
    FOLDER("1","文件夹"),
    FILE("2","文件");

    private String typeId;
    private String typeName;
    private static Map<String,MetaTypeEnum> idToEnumMap;
    static{
      Map<String,MetaTypeEnum> map  = new HashMap<>(MetaTypeEnum.values().length);
      for(MetaTypeEnum element : MetaTypeEnum.values()){
          map.put(element.getTypeId(),element);
      }
      idToEnumMap = Collections.unmodifiableMap(map);
    }
    MetaTypeEnum(String typeId,String typeName){
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }
    public static MetaTypeEnum getMetaTypeEnumById(String typeId){
        return idToEnumMap.get(typeId);
    }

}
