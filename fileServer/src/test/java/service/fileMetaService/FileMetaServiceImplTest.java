package service.fileMetaService;

import com.xinghai.fileServer.common.constant.MetaTypeEnum;
import com.xinghai.fileServer.common.constant.OrderEnum;
import com.xinghai.fileServer.common.constant.OrderFieldEnum;
import com.xinghai.fileServer.common.constant.StorageSourceEnum;
import com.xinghai.fileServer.common.util.DateUtil;
import com.xinghai.fileServer.domain.PO.FileMeta;
import com.xinghai.fileServer.domain.InterfaceParameter.fileMeta.CreateFolder;
import com.xinghai.fileServer.domain.InterfaceParameter.fileMeta.FilesAndFoldersUnderFolder;
import com.xinghai.fileServer.fileHandle.fileUtil.ManageFileMeta;
import com.xinghai.fileServer.fileHandle.fileMetaService.impl.FileMetaServiceImpl;
import com.xinghai.fileServer.fileHandle.fileMetaService.FileMetaService;
import org.junit.Before;
import util.TraversalUtil;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by scream on 2017/7/13.
 */

public class FileMetaServiceImplTest {
    FileMetaService fileMetaService;
    ManageFileMeta manageFileMeta;

    @Before
    public void init(){
        fileMetaService = new FileMetaServiceImpl();
        manageFileMeta = new ManageFileMeta();
    }

    //@Test
    //@Transient
    public void getAllFileMeta() throws Exception {

    }

    //@Test
    public void getFileMetaById() throws Exception {
        FileMeta fileMeta = fileMetaService.getFileMetaById(1);
        assertEquals("/", fileMeta.getName());
    }

    //@Test
    //@Transient
    public void createFile() throws Exception {
        FileMeta fileMeta = new FileMeta();
        fileMeta.setId(1);
        //fileMeta.setFileId(UUID.randomUUID().toString());
        fileMeta.setCreatedOn(DateUtil.parseDate("2017/7/28"));
        fileMeta.setCreatedBy("mh");
        fileMeta.setName("/");
        fileMeta.setType("1");
        fileMeta.setStorageSource(StorageSourceEnum.CASSANDRA.getTypeId());
        int result = manageFileMeta.createFile(fileMeta);
        System.out.println("affect rows :" + result);
        assertEquals(1, result);
    }

    //@Test
    public void getFileNameById() throws Exception {
        String name = fileMetaService.getFileNameById(47);
        assertEquals("jd.data.500w.sql",name);
    }

    //@Test
    public void deleteFileMetaByid(){
       int result = manageFileMeta.deleteFileMetaById(41);
       assertEquals(1,result);
    }

   //@Test
    public void createFolder(){
        CreateFolder createFolder = new CreateFolder();
        createFolder.setParentId(1);
        createFolder.setName("folder2");
        createFolder.setCreatedBy("test");
        createFolder.setCreatedOn(DateUtil.parseDate("2017/8/3"));
        createFolder.setType(MetaTypeEnum.FOLDER.getTypeId());
        int result = 0;
        try {
            result = fileMetaService.createFolder(createFolder);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        assertEquals(1,result);
    }

    //@Test
    public void deleteFolderTest(){
       int result = fileMetaService.deleteFolder(51);
       assertEquals(1,result);
    }

    //@Test
    public void getMetaTypeTest(){
        String type = fileMetaService.getTypeById(1);
        assertEquals("1",type);
    }

    //@Test
    public void getFilesAndFolderUnderFolderTest(){
        FilesAndFoldersUnderFolder filesAndFoldersUnderFolder = fileMetaService.getFilesAndFoldersUnderFolder(1,null,null);
        List<FileMeta> files = filesAndFoldersUnderFolder.getSubFilesList();
        System.out.println("------------files------------");
        TraversalUtil.traversalList(files);
        List<FileMeta> folders = filesAndFoldersUnderFolder.getSubFoldersList();
        System.out.println("-----------folders------------");
        TraversalUtil.traversalList(folders);
    }

    //@Test
    public void getFilesUnderFolderTest(){
        List<FileMeta> filesList = fileMetaService.getFilesUnderFolder(1, OrderFieldEnum.SIZE, OrderEnum.SEQUENCE);
        System.out.println("-----------files---------------");
        TraversalUtil.traversalList(filesList);
    }

    //@Test
    public void getFoldersUnderFolderTest(){
        List<FileMeta> folders = fileMetaService.getFolderUnderFolder(1,OrderFieldEnum.SIZE,OrderEnum.REVERSE);
        System.out.println("-----------folders------------");
        TraversalUtil.traversalList(folders);
    }

   // @Test
    public void updateNameTest(){
        int result = fileMetaService.updateName(61,"updat2e");
        assertEquals(1,result);
    }

    //@Test
    public void updateParentIdTest(){
        int result = fileMetaService.updateParentId(61,1);
        assertEquals(1,result);
    }
}