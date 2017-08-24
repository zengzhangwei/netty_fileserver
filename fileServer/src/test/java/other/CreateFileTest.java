package other;

import com.xinghai.fileServer.config.FileServerConfig;

import java.io.File;
import java.io.IOException;

/**
 * Created by scream on 2017/8/1.
 */
public class CreateFileTest {
    public static void main(String[] args){
        createLocalFile(null);
    }

    private static File createLocalFile(String name){
        File file = new File(FileServerConfig.FILE_DIR + File.separator + name);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("file exist!");
        }
        FileServerConfig.logger.info(file.getAbsolutePath());
        return file;
    }
}
