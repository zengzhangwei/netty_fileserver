package handleFile;

import com.xinghai.fileServer.domain.PO.FileMeta;
import com.xinghai.fileServer.fileHandle.fileContentService.HandleFile;
import com.xinghai.fileServer.fileHandle.fileContentService.impl.CassandraHandleFileImpl;
import com.xinghai.fileServer.domain.InterfaceParameter.cassandra.DeleteFile;
import com.xinghai.fileServer.fileHandle.fileMetaService.FileMetaService;
import com.xinghai.fileServer.fileHandle.fileMetaService.impl.FileMetaServiceImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Before;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by scream on 2017/8/1.
 */
public class CassandraHandleFileTest {
    HandleFile handleFile;
    FileMetaService fileMetaService;
    @Before
    public void init(){
        handleFile = new CassandraHandleFileImpl();
        fileMetaService = new FileMetaServiceImpl();
    }

    //@Test
    public void deleteFileTest(){
        FileMeta fileMeta = fileMetaService.getFileMetaById(62);
        DeleteFile deleteFile = new DeleteFile();
        try {
            BeanUtils.copyProperties(deleteFile, fileMeta);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        handleFile.deleteFile(deleteFile);
    }
}
