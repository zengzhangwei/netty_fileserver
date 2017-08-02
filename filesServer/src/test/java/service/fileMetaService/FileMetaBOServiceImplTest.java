package service.fileMetaService;

import com.xinghai.fileServer.common.constant.StorageSourceEnum;
import com.xinghai.fileServer.common.util.DateUtil;
import com.xinghai.fileServer.domain.BO.FileMetaBO;
import com.xinghai.fileServer.service.fileMetaService.impl.FileMetaServiceImpl;
import com.xinghai.fileServer.service.fileMetaService.FileMetaService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.beans.Transient;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by scream on 2017/7/13.
 */

public class FileMetaBOServiceImplTest {
    FileMetaService fileMetaService;
    @BeforeClass
    public void init(){
        fileMetaService = new FileMetaServiceImpl();
    }

    //@Test
    //@Transient
    public void getAllFileMeta() throws Exception {
        List<FileMetaBO> fileMetaBOS = fileMetaService.ListAllFileMeta();
        assertEquals(3, fileMetaBOS.size());
    }

    //@Test
    public void getFileMetaById() throws Exception {
        FileMetaBO fileMetaBO = fileMetaService.getFileMetaById(1);
        assertEquals("/", fileMetaBO.getName());
    }

    //@Test
    //@Transient
    public void createFile() throws Exception {
        FileMetaBO fileMetaBO = new FileMetaBO();
        fileMetaBO.setId(1);
        //fileMetaBO.setFileId(UUID.randomUUID().toString());
        fileMetaBO.setCreatedOn(DateUtil.parseDate("2017/7/28"));
        fileMetaBO.setCreatedBy("mh");
        fileMetaBO.setName("/");
        fileMetaBO.setType("1");
        fileMetaBO.setStorageSource(StorageSourceEnum.CASSANDRA.getTypeId());
        int result = fileMetaService.createFile(fileMetaBO);
        System.out.println("affect rows :" + result);
        assertEquals(1, result);
    }

    //@Test
    public void getFileNameById() throws Exception {
    }

    //@Test
    public void deleteFileMetaByid(){
       int result = fileMetaService.deleteFileMetaById(41);
       assertEquals(1,result);
    }
}