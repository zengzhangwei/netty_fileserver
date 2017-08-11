package other;

import com.xinghai.fileServer.common.constant.OrderEnum;
import com.xinghai.fileServer.common.constant.OrderFieldEnum;
import com.xinghai.fileServer.dao.mysqlDao.FileMetaProvider;
import com.xinghai.fileServer.domain.InterfaceParameter.fileMeta.ListFilesUnderFolder;

/**
 * Created by scream on 2017/8/3.
 */
public class FileMetaProviderTest {
    //@Test
    public void getFilesUnderFolder(){
        FileMetaProvider fileMetaProvider = new FileMetaProvider();
        ListFilesUnderFolder listFilesUnderFolder = new ListFilesUnderFolder();
        listFilesUnderFolder.setParentId(1);
        listFilesUnderFolder.setType("2");
        listFilesUnderFolder.setOrderSequence(OrderEnum.REVERSE.getId());
        listFilesUnderFolder.setOrderField(OrderFieldEnum.SIZE.getType());
        String sql = fileMetaProvider.getFilesUnderFolder(listFilesUnderFolder);
        System.out.println(sql);
    }
}
