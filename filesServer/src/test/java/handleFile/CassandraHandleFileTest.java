package handleFile;

import com.xinghai.fileServer.FileHandle.HandleFile;
import com.xinghai.fileServer.FileHandle.impl.CassandraHandleFileImpl;
import com.xinghai.fileServer.domain.BO.FileMetaBO;
import com.xinghai.fileServer.service.fileMetaService.FileMetaService;
import com.xinghai.fileServer.service.fileMetaService.impl.FileMetaServiceImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by scream on 2017/8/1.
 */
public class CassandraHandleFileTest {
    HandleFile handleFile;
    FileMetaService fileMetaService;
    @BeforeClass
    public void init(){
        handleFile = new CassandraHandleFileImpl();
        fileMetaService = new FileMetaServiceImpl();
    }

    //@Test
    public void deleteFileTest(){
        FileMetaBO fileMetaBO = fileMetaService.getFileMetaById(45);
        handleFile.deleteFile(fileMetaBO);
    }
}
