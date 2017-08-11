package com.xinghai.fileServer.common.util.file;

import com.xinghai.fileServer.config.FileServerConfig;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MixedFileUpload;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;

/**
 *注意：这个类中InterfaceHttpData数据只取一块会对后面存储有影响，因此数据要么全部遍历，要么不要拿部分数据块
 */
public class FileUtil {

    public static final String UNRECOGNITION_MIME = "application/octet-stream";
    /**
     *只最大读取一个Cassandra文件
     * @param data 待检测的文件块
     * @param name  文件名
     * @return 文件类型
     */
    public static String detectFileType(InterfaceHttpData data,String name){
        MixedFileUpload file = (MixedFileUpload)data;
        ByteBuf buf = null;
        try {
            buf = file.getChunk(FileServerConfig.TIKA_DETECT_SIZE);
        } catch (IOException e) {
            FileServerConfig.logger.info("读取待检测的文件块出错！");
            e.printStackTrace();
        }
        int realSize = buf.readableBytes();
        FileServerConfig.logger.info("the detect length is :" + realSize);
        byte[] detectChunk = new byte[realSize];
        buf.readBytes(detectChunk,0,realSize);
        buf.clear();
        buf.release();
        //buf.resetReaderIndex();
        FileServerConfig.logger.info("detect string is :" + new String(detectChunk));
        return detectMimeType(detectChunk,name);
    }

    public static String detectFileType(InterfaceHttpData data) throws IOException {
        MixedFileUpload curData = (MixedFileUpload) data;
        //ByteBuf bytebuf = curData.getByteBuf().copy();
        //ByteBuf bytebuf = curData.getByteBuf();
        ByteBuf bytebuf = curData.getChunk(FileServerConfig.TIKA_DETECT_SIZE);
        if(bytebuf.readableBytes() != curData.length() && bytebuf.readableBytes() != FileServerConfig.TIKA_DETECT_SIZE){
            bytebuf.clear();
            bytebuf.release();
            bytebuf = curData.getChunk(FileServerConfig.TIKA_DETECT_SIZE);
        }

        long dataLen = curData.length();
        int tikaLen = bytebuf.readableBytes();
        if (dataLen > FileServerConfig.TIKA_DETECT_SIZE) {
            tikaLen = bytebuf.readableBytes();
        }
        //Configuration.logger.info("data len "+curData.length()+"\ttika len "+tikaLen+"\treadable bytes "+bytebuf.readableBytes());
        byte[] byteArr = new byte[tikaLen];
        bytebuf.readBytes(byteArr, 0, tikaLen);
        bytebuf.release();
        bytebuf.clear();
        String mimeType = FileUtil.detectMimeType(byteArr, curData.getFilename());
        return mimeType;
    }


    public static String detectMimeType(byte[] data, String filename){
        Tika tika = new Tika();
        String mimeType = tika.detect(data, filename);
        if (mimeType.equalsIgnoreCase("application/octet-stream")){
            FileServerConfig.logger.info("未探测出文件MIME类型，默认为text/plain");
            mimeType = "text/plain";
        }
        return mimeType;
    }

    public static String detectMimeType(File file) {
        Tika tika = new Tika();
        String mimeType = null;
        try {
            mimeType = tika.detect(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mimeType.equalsIgnoreCase("application/octet-stream")){
            FileServerConfig.logger.info("未探测出文件MIME类型，默认为text/plain");
            mimeType = "text/plain";
        }
        return mimeType;
    }
}
