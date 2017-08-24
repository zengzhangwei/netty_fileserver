package com.xinghai.fileServer.common.constant;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by scream on 2017/8/3.
 */
public enum OrderFieldEnum {
    //创建时间
    CREATEDON(1,"created_on"),
    //修改时间
    MODIFIEDON(2,"modified_on"),
    //名称
    NAME(3,"name"),
    //文件大小
    SIZE(4,"size");
    private Integer id;
    private String type;
    private static Map<Integer ,OrderEnum> idToOrderEnum;
    OrderFieldEnum(Integer id,String type){
        this.id = id;
        this.type = type;
    }

    static {
        Map<Integer,OrderEnum>  map = new HashMap<>(OrderEnum.values().length);
        for(OrderEnum element:OrderEnum.values()){
            map.put(element.getId(),element);
        }
        idToOrderEnum = Collections.unmodifiableMap(map);
    }

    public Integer getId() {
        return id;
    }


    public String getType() {
        return type;
    }

    public static OrderEnum getOrderEnumByid(Integer id){
        return idToOrderEnum.get(id);
    }
}
