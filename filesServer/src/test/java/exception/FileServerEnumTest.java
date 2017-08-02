package exception;


import com.xinghai.fileServer.common.exception.FileServerErrorEnum;
import com.xinghai.fileServer.common.exception.FileServerException;

/**
 * Created by scream on 2017/7/17.
 */
public class FileServerEnumTest {
    public static void main(String[] args){
       // FileServerErrorEnum fse = FileServerErrorEnum.getFileServerEnumByCode(1000);
        FileServerException fse = new FileServerException("test", FileServerErrorEnum.UNKNOWN);
        System.out.println(" the errno "+ fse.getCode() +" and the reason is:" + fse.getMessage());
    }
}
