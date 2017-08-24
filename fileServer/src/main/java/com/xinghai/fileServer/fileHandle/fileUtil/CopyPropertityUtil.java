package com.xinghai.fileServer.fileHandle.fileUtil;

import com.xinghai.fileServer.common.exception.FileServerErrorEnum;
import com.xinghai.fileServer.common.exception.FileServerException;
import com.xinghai.fileServer.config.FileServerConfig;
import com.xinghai.fileServer.domain.InterfaceParameter.cassandra.CreateFile;
import com.xinghai.fileServer.domain.PO.FileMeta;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by scream on 2017/8/17.
 */
public class CopyPropertityUtil {
    /**
     *
     * @param dst 属性将被赋值对象
     * @param source 属性将被复制对象
     */
    public static void copyProperties(Object dst, Object source){
        try {
            BeanUtils.copyProperties(dst, source);
        } catch (IllegalAccessException |InvocationTargetException e) {
            e.printStackTrace();
            FileServerConfig.logger.info("拷贝对象属性时出错！");
            throw new FileServerException(FileServerErrorEnum.COPY_BEAN_PROPERTITIE_ERROR);
        }
    }
}
