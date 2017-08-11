package other;

import com.xinghai.fileServer.config.FileServerConfig;

import java.io.File;
import java.io.IOException;

/**
 * Created by scream on 2017/8/1.
 */
public class CreateFileTest {
    private File createLocalFile(String name){
        File file = new File("File/"+ name);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileServerConfig.logger.info(file.getAbsolutePath());
        return file;
    }
}
