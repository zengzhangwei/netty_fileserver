package common.util;

import com.xinghai.fileServer.common.util.DateUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by scream on 2017/7/12.
 */
public class DateUtilTest {
    public static final Logger logger = LogManager.getLogger(DateUtilTest.class);
    //@Test
    public void date2String() throws Exception {
       // Date now = new Date();
        String oldDate = "2016/12/03";
        Date dataTime = DateUtil.parseDate(oldDate);
        String result = DateUtil.Date2String(dataTime,"yyyy-MM-dd");
        //assertThat(result).isEqualTo("2016-12-03");
        logger.info("the transform is : "+ result);
       // System.out.println("the transform is : "+ result);
    }

    //@Test
    public void date2String1() throws Exception {
    }

    //@Test
    public void parseDate() throws Exception {
        String oldDate = "2016-12-3";
        Date dataTime = DateUtil.parseDate(oldDate);
       // assertThat(dataTime.getClass()).isEqualTo(java.util.Date);
        System.out.println("the type is :" + dataTime.getClass());
    }

    //@Test
    public void changeMonth() throws Exception {
    }

}