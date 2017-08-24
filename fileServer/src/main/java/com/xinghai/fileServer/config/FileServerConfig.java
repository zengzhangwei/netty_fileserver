package com.xinghai.fileServer.config;

import com.xinghai.fileServer.common.util.JsonUtil;
import com.xinghai.fileServer.service.nettyInit.HttpFileServerHandler;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by scream on 2017/7/13.
 * 文件服务配置类
 */
public class FileServerConfig {
    /**
     * 以下静态字段的赋值均为默认值，可被配置文件覆盖
     */
    public static  List<String> CASSANDRA_IP = null; // cassandra节点的IP
    public static  String HOST_IP = "127.0.0.1"; //服务器节点的IP
    public static  int HOST_PORT = 8080; //服务节点的port

    public static  String FILE_DIR = "C:\\Users\\scream\\Desktop\\fileDir";//上传的文件保存目录
    public static  String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz"; //日期格式
    public static  String HTTP_DATE_GMT_TIMEZONE = "GMT";  //日期为格林威治标准时间
    public static  int HTTP_CACHE_SECONDS = 60;        //HTTP缓存时间

    public static int CHUNKSIZE = 1 * 1024 * 1024;
    public static int SIZE_IN_MEMORY = CHUNKSIZE * 10; //超过多大则存盘，否则利用内存即可
    //private static long SIZE_IN_MEMORY = DefaultHttpDataFactory.MINSIZE;
    //每次拿出的文件长度
    public static int GET_DATA_LENGTH_EACH_TIME = CHUNKSIZE * 10;
    //以数组形式下载文件最大文件长度
    public static long MAX_FILE_LENGTH_IN_MEMMORY = 100 * 1024 * 1024;

    public static String BASEDIRECTORY_FILEUPLOAD = null; //"D:\\CODE\\temp";
    public static String BASEDIRECTORY_ATTRIBUTE = null; //"D:\\CODE\\temp";

    //检测大小
    public static int TIKA_DETECT_SIZE = 10 * CHUNKSIZE;

    //开发测试时临时文件放置目录
    public static String TEMP_FILE_DIR = "C:\\Users\\scream\\Desktop\\temp";

    //可用cpu
    public static  int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors() * 2;
    //线程数量
    public static  int BIZTHREADSIZE = 100;

    //public static final int DOWNLOADSIZE = CHUNKSIZE;

    public static final Logger logger = Logger.getLogger(HttpFileServerHandler.class);

    //完成初始化工作
    static {
        //读取.json配置文件配置参数
        InputStream is = FileServerConfig.class.getClassLoader().getResourceAsStream("config.json");
        if(is == null){
            System.out.println("未找到config.json配置文件,强行终止！");
            System.exit(1);
        }
        ConfigurationMatchJSON configurationMatchJSON = JsonUtil.fromInputStream(is,ConfigurationMatchJSON.class);
        //配置cassandra ip
        CASSANDRA_IP = configurationMatchJSON.getCassandra();
        HOST_IP = configurationMatchJSON.getHostIp();
        HOST_PORT = configurationMatchJSON.getHostPort();
        FILE_DIR =configurationMatchJSON.getFileDir();
        HTTP_CACHE_SECONDS =configurationMatchJSON.getHttpCacheSeconds();
        CHUNKSIZE = configurationMatchJSON.getChunkSize();
        SIZE_IN_MEMORY = configurationMatchJSON.getSizeInMemory();
        GET_DATA_LENGTH_EACH_TIME = configurationMatchJSON.getGetDataLengthEachTime();
        MAX_FILE_LENGTH_IN_MEMMORY = configurationMatchJSON.getMaxFileLengthInMemory();
        TIKA_DETECT_SIZE = configurationMatchJSON.getTikaDetectSize();
        TEMP_FILE_DIR = configurationMatchJSON.getTempFileDir();
        BIZTHREADSIZE = configurationMatchJSON.getBizThreadSize();
        logger.info("read the configuration parameter finished !");
        //System.out.println("host_ip "+ configurationMatchJSON.getHostIp());

        //创建服务端存放文件的文件夹
        File file = new File(FILE_DIR);
        if (!file.exists()) file.mkdir();
        //创建临时文件夹
        File tempDir = new File(TEMP_FILE_DIR);
        if (!file.exists()) tempDir.mkdirs();

    }


}
