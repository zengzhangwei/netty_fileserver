package handleFile;

import com.xinghai.fileServer.domain.InterfaceParameter.cassandra.DeleteFile;
import com.xinghai.fileServer.domain.PO.FileMeta;
import com.xinghai.fileServer.fileHandle.fileContentService.HandleFile;
import com.xinghai.fileServer.fileHandle.fileContentService.impl.LocalFileServerHandleFile;
import com.xinghai.fileServer.fileHandle.fileMetaService.FileMetaService;
import com.xinghai.fileServer.fileHandle.fileMetaService.impl.FileMetaServiceImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by scream on 2017/8/18.
 */
public class LocalFileServerHandleFileTest {
    HandleFile handleFile;
    FileMetaService fileMetaService;
    @Before
    public void init(){
        handleFile = new LocalFileServerHandleFile();
        fileMetaService = new FileMetaServiceImpl();
    }

    //@Test
    public void deleteFileTest(){
        FileMeta fileMeta = fileMetaService.getFileMetaById(86);
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
