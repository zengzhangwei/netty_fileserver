package other;

import com.xinghai.fileServer.domain.PO.FileMeta;
import com.xinghai.fileServer.domain.InterfaceParameter.cassandra.CreateFile;
import domain.Bean1;
import domain.Bean2;
import domain.Student;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by scream on 2017/7/27.
 * 简单测试下BeanUtils用法
 */
public class BeanTest {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
      //  simpleCopyProperties();
       // copyObjectProperties();

        CreateFile createFile = new CreateFile();
        createFile.setName("test");
        createFile.setId(12);
        FileMeta fileMeta = new FileMeta();
        copyProperties(fileMeta,createFile);
        System.out.println("fileMeta name: " + fileMeta.getName());


    }

    public static void simpleCopyProperties() throws InvocationTargetException, IllegalAccessException {
        CreateFile createFile = new CreateFile();
        createFile.setParentId(1);
        createFile.setName("po");
        FileMeta fileMeta = new FileMeta();
        BeanUtils.copyProperties(fileMeta, createFile);
        System.out.println("after copy:" + fileMeta.toString());
        //result:after copy:FileMeta{id=null, fileId='null', blockCount=0, parentId=1, createdBy='null', createdOn=null, modifiedBy='null', modifiedOn=null, name='po', size=null, contentType='null', hash='null', type='null', storageSource=null}

        createFile.setParentId(3);
        System.out.println("after change:"+ createFile.toString());
        //after change:CreateFile{id=null, parentId=3, createdBy='null', createdOn=null, modifiedBy='null', modifiedOn=null, name='po', size=null, contentType='null', hash='null', type='null', storageSource=null}
        System.out.println("after change :" + fileMeta.toString());
        //after change :FileMeta{id=null, fileId='null', blockCount=0, parentId=1, createdBy='null', createdOn=null, modifiedBy='null', modifiedOn=null, name='po', size=null, contentType='null', hash='null', type='null', storageSource=null}
    }

    public static void copyObjectProperties() throws InvocationTargetException, IllegalAccessException {
        Student student = new Student();
        student.setName("test1");
        student.setAge(23);

        Bean1 bean1 = new Bean1();
        bean1.setId("1");
        bean1.setStudent(student);

        Bean2 bean2 = new Bean2();

        System.out.println("before copy:" + bean1.toString());
        //before copy:Bean1{id='1', student=Student{name='test1', age=23}}
        BeanUtils.copyProperties(bean2,bean1);

        student.setName("test2");
        bean1.setId("2");

        System.out.println("after copy" + bean2.toString());
        //after copyBean2{id='1', student=Student{name='test2', age=23}}

        //结论，对于对象类型,复制的只是对象的地址
    }

    private static void copyProperties(Object fileMeta, Object createFile){
        try {
            BeanUtils.copyProperties(fileMeta, createFile);
        } catch (IllegalAccessException |InvocationTargetException e) {
            e.printStackTrace();
           // FileServerConfig.logger.info("拷贝对象属性时出错！");
            //throw new FileServerException(FileServerErrorEnum.COPY_BEAN_PROPERTITIE_ERROR);
        }
    }
}
