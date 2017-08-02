package com.xinghai.fileServer.service.httpService;

import com.xinghai.fileServer.common.Response;
import com.xinghai.fileServer.common.exception.FileServerErrorEnum;
import com.xinghai.fileServer.common.exception.FileServerException;
import com.xinghai.fileServer.common.util.JsonUtil;
import com.xinghai.fileServer.domain.BO.FileMetaBO;
import com.xinghai.fileServer.service.fileMetaService.impl.FileMetaServiceImpl;
import com.xinghai.fileServer.service.fileMetaService.FileMetaService;
import com.xinghai.fileServer.service.nettyInit.FileServerConfig;
import com.xinghai.fileServer.service.nettyInit.HttpProcessor;
import com.xinghai.fileServer.service.response.ResponseWriter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by scream on 2017/7/13.
 */
public class HttpGetFileMeta  extends HttpProcessor {

    private List<byte[]> batchInfos = new LinkedList<>();
    private int length = 0;
    FileMetaService fileMetaService = new FileMetaServiceImpl();

    @Override
    protected void processHttpRequest(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        super.processHttpRequest(ctx, httpRequest);
    }
    /**
     * 从普通post请求中的content获取参数
     */
    @Override
    protected void processHttpContent(ChannelHandlerContext ctx, HttpContent httpContent) {
        ByteBuf buffer = httpContent.content();
        byte[] jsonbyte = new byte[buffer.readableBytes()];
        buffer.readBytes(jsonbyte);

        batchInfos.add(jsonbyte);
        length += jsonbyte.length;
        //判断是否全部接受完
        if (!(httpContent instanceof LastHttpContent)) {
            return;
        }

        byte[] command_bytearr = new byte[length];
        int idx = 0;
        for (byte[] currByte : batchInfos) {
            for (int i = 0; i < currByte.length; i++) {
                command_bytearr[idx++] = currByte[i];
            }
        }
         String jsonStr = new String(command_bytearr);
         FileServerConfig.logger.info("param from getFileMetaById: "+jsonStr);
         Integer id = JsonUtil.fromJsonNode(jsonStr,"id",Integer.class);
         FileMetaBO fileMetaBO = fileMetaService.getFileMetaById(id);
         if (fileMetaBO == null){
             FileServerConfig.logger.info(FileServerErrorEnum.FILE_NOT_EXIST.getMessage());
             throw new FileServerException(FileServerErrorEnum.FILE_NOT_EXIST);
             //ResponseWriter.writeResponseJSON(request, ctx.channel(), JsonUtil.toJson(new Response<FileMetaBO>(FileServerErrorEnum.FILE_NOT_EXIST.getCode(),FileServerErrorEnum.FILE_NOT_EXIST.getMessage())));
         }
         Response<FileMetaBO> response = new Response<>(0,"success", fileMetaBO);
         ResponseWriter.writeResponseJSON(request, ctx.channel(), JsonUtil.toJson(response));
    }
}

