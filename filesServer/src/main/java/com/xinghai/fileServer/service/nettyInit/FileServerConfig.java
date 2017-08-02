package com.xinghai.fileServer.service.nettyInit;

import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by scream on 2017/7/13.
 * 文件服务配置类
 */
public class FileServerConfig {
    public static final String FILE_DIR = "/data/tmp";//上传的文件保存目录
    public static final String CASSANDRA_IP = "172.24.65.144"; // cassandra节点的IP
    public static final String HOST_IP = "127.0.0.1"; //服务器节点的IP
    public static final int HOST_PORT = 8080; //服务节点的port

    public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz"; //日期格式
    public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";  //日期为格林威治标准时间
    public static final int HTTP_CACHE_SECONDS = 60;        //HTTP缓存时间

    public static int CHUNKSIZE = 1 * 1024 * 1024;
    public static long DISKSIZE = CHUNKSIZE * 10; //超过多大则存盘，否则利用内存即可
    //private static long DISKSIZE = DefaultHttpDataFactory.MINSIZE;
    public static int CASSANDRABLOCK = CHUNKSIZE * 10;

    public static String BASEDIRECTORY_FILEUPLOAD = null; //"D:\\CODE\\temp";
    public static String BASEDIRECTORY_ATTRIBUTE = null; //"D:\\CODE\\temp";

    public static int TIKA_DETECT_SIZE = 10 * CHUNKSIZE;

    //开发测试时零时文件放置目录
    public static String TEMP_FILE_DIR = "C:\\Users\\scream\\Desktop\\temp";

    //
    public static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors() * 2;
    //线程数量
    public static final int BIZTHREADSIZE = 100;

    public static final int DOWNLOADSIZE = CHUNKSIZE;

    static {
        File file = new File(FILE_DIR);
        if (!file.exists()) file.mkdir();
    }

    public static final Logger logger = Logger.getLogger(HttpFileServerHandler.class);
}
