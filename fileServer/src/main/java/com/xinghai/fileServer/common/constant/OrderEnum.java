package com.xinghai.fileServer.common.constant;

import com.datastax.driver.core.TableMetadata;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by scream on 2017/8/3.
 */
public enum OrderEnum {
    SEQUENCE(1,"正序"),
    REVERSE(2,"倒序");
    private Integer id;
    private String type;
    private static Map<Integer ,OrderEnum> idToOrderEnum;
    OrderEnum(Integer id,String type){
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
