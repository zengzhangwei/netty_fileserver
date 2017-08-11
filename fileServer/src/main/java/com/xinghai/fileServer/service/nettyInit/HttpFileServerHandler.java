package com.xinghai.fileServer.service.nettyInit;

import com.xinghai.fileServer.common.Response;
import com.xinghai.fileServer.common.exception.BaseException;
import com.xinghai.fileServer.common.exception.FileServerErrorEnum;
import com.xinghai.fileServer.common.exception.FileServerException;
import com.xinghai.fileServer.common.util.JsonUtil;
import com.xinghai.fileServer.config.FileServerConfig;
import com.xinghai.fileServer.service.httpService.HttpDownLoadProcessor;
import com.xinghai.fileServer.service.httpService.HttpFileImportProcessor;
import com.xinghai.fileServer.service.httpService.HttpGetFileMeta;
import com.xinghai.fileServer.service.httpService.HttpOptionsProcessor;
import com.xinghai.fileServer.service.response.ResponseWriter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpMethod.OPTIONS;
import static io.netty.handler.codec.http.HttpMethod.POST;

/**
 * 接口处理类
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    public final static Map<RequestMappingInfo, Class<? extends HttpProcessor>> processorMap = new HashMap<RequestMappingInfo ,  Class<? extends  HttpProcessor>>(){
        {
           put(new RequestMappingInfo("getFileMetaById", POST), HttpGetFileMeta.class);
           put(new RequestMappingInfo("fileImport",POST),HttpFileImportProcessor.class);
           put(new RequestMappingInfo("fileDownload",GET), HttpDownLoadProcessor.class);
        }};

    private static Pattern pattern = Pattern.compile("[^/][a-zA-Z]*");

    private HttpProcessor httpProcessor;
    //标识初始化处理程序是否成功
    private boolean success = false;
    //测试服务是否启动参数的请求
    private boolean isOptions = false;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        FileServerConfig.logger.info("channelRegistered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx){
        FileServerConfig.logger.info("channelUnregistered");
        if (httpProcessor != null) {
            httpProcessor.channelUnregistered(ctx);
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {
          try {
            if (httpObject instanceof HttpRequest) {
                HttpRequest httpRequest = (HttpRequest) httpObject;
                FileServerConfig.logger.info("*** Process Http request ***" + "Method:" + httpRequest.getMethod() + "\tContent-len:" + httpRequest.headers().get(CONTENT_LENGTH));
                success =initHttpProcessor(ctx, httpRequest);
                if (!isOptions && success) {
                    Map<String, String> requestParams = parseRequestParameters(httpRequest);
                    this.httpProcessor.setRequestParams(requestParams);
                    this.httpProcessor.processHttpRequest(ctx, httpRequest);
                }
            } else if (httpObject instanceof HttpContent) {
                //FileServerConfig.logger.info("int the handle the content length is:" + ((HttpContent) httpObject).content().readableBytes());
                if (isOptions) {
                    isOptions = false;
                    ctx.channel().close();
                    return;
                }
                if(success) {
                    this.httpProcessor.processHttpContent(ctx, (HttpContent) httpObject);
                }else{
                    FileServerConfig.logger.error("处理程序未初始化");
                    throw  new FileServerException(FileServerErrorEnum.REQUEST_PATH_NOT_EXIST);
                }
            } else {
                FileServerConfig.logger.error("Unknown http object");
                throw new FileServerException(FileServerErrorEnum.UNKNOWN_HTTP_OBJECT);
                // ResponseWriter.writeResponseJSON(httpProcessor.request, ctx.channel(), JsonUtil.toJson(new Response<>(FileServerErrorEnum.UNKNOWN_HTTP_OBJECT.getCode(), FileServerErrorEnum.UNKNOWN_HTTP_OBJECT.getMessage())));
            }
          } catch (BaseException e) {
            FileServerConfig.logger.error(e.getMessage());
            ResponseWriter.writeResponseJSON(httpProcessor.request, ctx.channel(), JsonUtil.toJson(new Response<>(e.getCode(),e.getMessage())));
            ctx.channel().close();
            ctx.close();
            return;
        }
    }

    /**
     * 解析url参数
     * @param request
     * @return
     */
    private Map<String, String> parseRequestParameters(HttpRequest request) {
        Map<String, String> requestParams = new HashMap<>();
        // 处理get请求
        if (request.getMethod() == GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
            Map<String, List<String>> parame = decoder.parameters();
            Iterator<Map.Entry<String, List<String>>> iterator = parame.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<String>> next = iterator.next();
                requestParams.put(next.getKey(), next.getValue().get(0));
            }
        }
        return requestParams;
    }

    /**
     * 初始化处理程序,注意此时request未被赋值给处理程序，所以出错了应该直接返回给客户端，
     * 不能抛给上面的BaseException处理
     * @param ctx
     * @param request
     * @return
     */
    private boolean initHttpProcessor(ChannelHandlerContext ctx, HttpRequest request) {
        try {
            URI uri = new URI(request.getUri());
            FileServerConfig.logger.info("get the uri is "+ uri.toString());
            Matcher m = pattern.matcher(uri.getPath());
            if(!m.find()){
                return false;
            }
            String requestPath = m.group(); // 请求类型赋值
            Class<? extends HttpProcessor> processorClazz = null;
            boolean isRequestPathExist = false;
            for (Map.Entry<RequestMappingInfo, Class<? extends  HttpProcessor>> entry : processorMap.entrySet()) {
                if (entry.getKey().getPatterns().contains(requestPath)) {
                    isRequestPathExist = true;
                    //处理Options
                    if(request.getMethod().equals(OPTIONS)){
                        HttpOptionsProcessor.doOptions(ctx, request);
                        isOptions = true;
                    }else if (entry.getKey().getMethods().contains(request.getMethod())) {
                        processorClazz = entry.getValue();
                    } else {
                          String  returnMsg= JsonUtil.toJson(new Response<>(FileServerErrorEnum.PATH_CAN_NOT_MATCH_METHOD.getCode(),FileServerErrorEnum.PATH_CAN_NOT_MATCH_METHOD.getMessage()));
                          String info = request.getMethod().name() + " method not support!";
                          FileServerConfig.logger.info(returnMsg);
                          FileServerConfig.logger.info(info);
                          ResponseWriter.writeResponse(request, ctx.channel(),returnMsg);
                    }
                    break;
                }
            }
            if (isRequestPathExist) {
                if (processorClazz != null) {
                    this.httpProcessor = processorClazz.newInstance();
                    return true;
                }
            } else {
                String  returnMsg = JsonUtil.toJson(new Response<>(FileServerErrorEnum.REQUEST_PATH_NOT_EXIST.getCode(),FileServerErrorEnum.REQUEST_PATH_NOT_EXIST.getMessage()));
                FileServerConfig.logger.info(returnMsg);
                ResponseWriter.writeResponse(request, ctx.channel(),returnMsg);
            }
        } catch (Exception e) {
            FileServerConfig.logger.error(e.getMessage(), e);
            String  returnMsg = JsonUtil.toJson(new Response<>(FileServerErrorEnum.UNKNOWN.getCode(),FileServerErrorEnum.UNKNOWN.getMessage()));
            FileServerConfig.logger.error(returnMsg);
            ResponseWriter.writeResponse(request, ctx.channel(),returnMsg);
        }
        return false;
    }

    //异常处理,只有程序中未捕获到的异常会在此处抛出
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        FileServerConfig.logger.error(cause.getMessage());
        ResponseWriter.writeResponseJSON(httpProcessor.request, ctx.channel(), JsonUtil.toJson(new Response<>(FileServerErrorEnum.UNKNOWN.getCode(),FileServerErrorEnum.UNKNOWN.getMessage())));
       // ctx.channel().close();
    }
}
