package com.xinghai.fileServer.service.httpService;

import com.xinghai.fileServer.domain.BO.FileMetaBO;
import com.xinghai.fileServer.service.fileMetaService.FileMetaService;
import com.xinghai.fileServer.service.fileMetaService.impl.FileMetaServiceImpl;
import com.xinghai.fileServer.service.nettyInit.FileServerConfig;
import com.xinghai.fileServer.service.nettyInit.HttpProcessor;
import com.xinghai.fileServer.service.response.HttpResponseUtils;
import com.xinghai.fileServer.service.response.ResponseWriterDownload;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Created by scream on 2017/7/27.
 */
public class HttpDownLoadProcessor extends HttpProcessor {

    private String localfilename = "C:\\Users\\scream\\Desktop\\gradle-4.0-all.zip";
    @Override
    protected void processHttpRequest(ChannelHandlerContext ctx, HttpRequest httpRequest){
        super.processHttpRequest(ctx, httpRequest);
        FileServerConfig.logger.info("Do Download Get!");
        //文件下载
        //Long id = Long.parseLong(requestParams.get("id"));
        Integer id = Integer.parseInt(requestParams.get("id"));
        //FileServerConfig.logger.info("the id is :" + id);
        String filePath = requestParams.get("filePath");
        FileMetaService fileMetaService = new FileMetaServiceImpl();

        FileMetaBO file = fileMetaService.getFileMetaById(Integer.valueOf(id));
        //下载文件这个地方的异常应该直接抛给前端,否则无法判断是否正常下载文件，导致将错误信息写进文件
        if (file == null) {
            FileServerConfig.logger.info("文件id为："+ id+ "未找到" );
            HttpResponseUtils.sendError(ctx, HttpResponseStatus.NOT_FOUND);
            //throw new FileServerException(FileServerErrorEnum.FILE_NOT_EXIST);
        }
       // FileServerConfig.logger.info("from file meta :"+file.getSize());
        ResponseWriterDownload.writeDownLoadResponse(ctx,file,request);
    }

    @Override
    protected void processHttpContent(ChannelHandlerContext ctx, HttpContent httpContent) {

    }
}
