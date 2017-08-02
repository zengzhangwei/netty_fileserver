package com.xinghai.fileServer.service.httpService;

import com.xinghai.fileServer.common.Response;
import com.xinghai.fileServer.common.constant.MetaTypeEnum;
import com.xinghai.fileServer.common.constant.StorageSourceEnum;
import com.xinghai.fileServer.common.exception.FileServerErrorEnum;
import com.xinghai.fileServer.common.exception.FileServerException;
import com.xinghai.fileServer.common.util.JsonUtil;
import com.xinghai.fileServer.common.util.file.FileUtil;
import com.xinghai.fileServer.domain.BO.FileMetaBO;
import com.xinghai.fileServer.domain.VO.CreateFileVO;
import com.xinghai.fileServer.FileHandle.HandleFile;
import com.xinghai.fileServer.service.nettyInit.FileServerConfig;
import com.xinghai.fileServer.service.nettyInit.HttpFileProcessor;
import com.xinghai.fileServer.service.response.ResponseWriter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.*;

import com.xinghai.fileServer.FileHandle.impl.CassandraHandleFileImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by scream on 2017/7/19.
 */
public class HttpFileImportProcessor extends HttpFileProcessor {

    private int count = 0;
    private long countLength = 0;
    private Map<String,String> parameters = new HashMap<String,String>();
    private CreateFileVO createFileVO = new CreateFileVO();
    @Override
    protected void processHttpRequest(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        super.processHttpRequest(ctx, httpRequest);
    }

    @Override
    protected void processHttpContent(ChannelHandlerContext ctx, HttpContent httpContent) {
        if (httpDecoder != null) {
            // FileServerConfig.logger.info("int the ContentHandle the content length is:" + httpContent.content().readableBytes());
            countLength += httpContent.content().readableBytes();
            httpDecoder.offer(httpContent);
            // InterfaceHttpData chunk = httpDecoder.getBodyHttpData("fileUpload");
            // FileServerConfig.logger.info("chunk length"+((HttpData)chunk).length());
            //从解码器中拿出数据并解析
            readHttpDataChunkByChunk();
            if (httpContent instanceof LastHttpContent) {
                //FileServerConfig.logger.info("total length:" + countLength);
                ResponseWriter.writeResponseJSON(request, ctx.channel(), JsonUtil.toJson(new Response<>()));
            }
        } else {
            throw new FileServerException(FileServerErrorEnum.UNRECEIVE_NO_DATA);
        }
    }

    private void readHttpDataChunkByChunk() {
        while (httpDecoder.hasNext()) {
            InterfaceHttpData data = httpDecoder.next();
            if (data != null) {
                FileServerConfig.logger.info("the chunk datatype is :" + data.getHttpDataType().toString() + " and the data length is:" + ((HttpData)data).length());
                try {
                    if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                        Attribute attribute = (Attribute) data;  //Attribute 继承自HttpData
                        String name = attribute.getName();
                        String value = attribute.getValue();
                        parameters.put(name,value);
                       // FileServerConfig.logger.info("the " + (++count) + " times come and attribute is :" + name + " and the value is: " + value);
                    } else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                       // FileServerConfig.logger.info("the file name is" + ((FileUpload)data).getFilename()+"the file length is :" + ((FileUpload)data).length());
                        //将参数放进fileMeta对象中
                        putRequestParametersIntoFileMeta();
                        //文件属性赋值，如文件大小，文件类型，上传文件时所带参数等
                        FileMetaBO fileMetaBO = new FileMetaBO();
                        saveTheFileProperty(fileMetaBO,data);
                        //保存文件
                        HandleFile handleFile = new CassandraHandleFileImpl();
                        handleFile.saveFile(fileMetaBO,data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new FileServerException(FileServerErrorEnum.READ_FILE_CHUNK_ERROR);
                } finally {
                    data.release();
                }
            }
        }
    }

    /**
     * 设置parameter
     */
    private void putRequestParametersIntoFileMeta(){
        String value = null;
         for(HashMap.Entry<String,String> parameter: parameters.entrySet()){
             value = parameter.getValue();
             switch (parameter.getKey()){
                 case "parentDirId":
                     createFileVO.setParentDirId(Integer.valueOf(value));
                     break;
                 case "filePath":
                     createFileVO.setFilePath(value);
                     break;
                 default:
                     FileServerConfig.logger.info("上传文件时有未处理的属性");
                     break;
             }
        }
    }

    //提取文件相关信息
    private void saveTheFileProperty(FileMetaBO fileMetaBO,InterfaceHttpData data){
          fileMetaBO.setParentId(createFileVO.getParentDirId());
          fileMetaBO.setName(((FileUpload) data).getFilename());
          fileMetaBO.setSize(((FileUpload) data).length());
          fileMetaBO.setStorageSource(StorageSourceEnum.CASSANDRA.getTypeId());
          fileMetaBO.setType(MetaTypeEnum.FILE.getTypeId());
          fileMetaBO.setContentType(((FileUpload)data).getContentType());
    }
}
