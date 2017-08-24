package other;

import com.xinghai.fileServer.common.constant.MetaTypeEnum;
import com.xinghai.fileServer.common.exception.FileServerException;
import com.xinghai.fileServer.common.util.DateUtil;
import com.xinghai.fileServer.domain.InterfaceParameter.fileMeta.CreateFolder;
import com.xinghai.fileServer.fileHandle.fileMetaService.FileMetaService;
import com.xinghai.fileServer.fileHandle.fileMetaService.impl.FileMetaServiceImpl;

import static org.junit.Assert.assertEquals;

/**
 * Created by scream on 2017/8/3.
 */
public class CreateFolderExceptionTest {
    public static void main(String[] args){
        createFolder();
    }
    public static void createFolder(){
        FileMetaService fileMetaService = new FileMetaServiceImpl();
        CreateFolder createFolder = new CreateFolder();
        createFolder.setParentId(1);
        createFolder.setName("folder1");
        createFolder.setCreatedBy("test");
        createFolder.setCreatedOn(DateUtil.parseDate("2017/8/3"));
        createFolder.setType(MetaTypeEnum.FOLDER.getTypeId());

        int result = 0;
        try {
            result = fileMetaService.createFolder(createFolder);
        }catch (FileServerException e){
            System.out.println("get the exception message:");
            System.out.println(e.getMessage());
        }
        assertEquals(1,result);
    }
}
