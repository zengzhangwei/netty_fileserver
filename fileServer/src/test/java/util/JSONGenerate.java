package util;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


/**
 * Created by lixiaodong on 2016/7/15.
 */
public class JSONGenerate {
    //private String localfilename = "D:\\CODE\\fyfeng-NettyUploadExample-master.zip";
    public static String localfilename = "D:\\CODE\\test.txt";
    //private String localfilename =  "D:\\CODE\\Sublime Text 2.0.1 x64 Setup.exe";
    //private String localfilename = "D:\\CODE\\jdk_1.8.0.0_64.exe";
    //private String localfilename = "D:\\CODE\\testpackage.zip";
    //private String localfilename = "d:\\download\\u=1694784099,2343118919&fm=15&gp=0.jpg";


    public static int user_id = 1;
    private static String user_name  = "xiaoming";
    private static String user_ip = "127.0.0.1";

    public static String getFileMetaById() throws JSONException {
        JSONStringer jsonStringer = new JSONStringer();
        jsonStringer.object().key("id").value("46").endObject();
        return jsonStringer.toString();
    }

    public static String sendBatchInfo(String batch_id,List<String> arr) throws JSONException {
        JSONStringer jsonStringer = new JSONStringer();
        jsonStringer.object().key("command").value("send_batch_info").key("config").object()
                .key("userId").value(user_id)
                .key("userName").value(user_name)
                .key("userIP").value(user_ip)
                .key("batchId").value(batch_id.toString())
                .key("batchTime").value(System.currentTimeMillis())
                .key("files").array();
        for(int i = 0 ; i < arr.size() ; i ++){
            File file = new File(arr.get(i));
            jsonStringer.object().key("name").value(arr.get(i))
                    .key("path").value(arr.get(i))
                    .key("size").value(file.length())
                    .key("time").value(System.currentTimeMillis())
                    .endObject();
        }
        jsonStringer.endArray().endObject().endObject();
        return jsonStringer.toString();
    }

    public static String getBatchInfo( String status) throws JSONException {
        JSONStringer jsonStringer = new JSONStringer();
        jsonStringer.object().key("command").value("get_batch_info").key("config").object()
                .key("userId").value(user_id)
                .key("status").value(status)
                .endObject().endObject();
        return jsonStringer.toString() ;
    }

    public static String getFileByBatchIdFail(String  batch_id) throws JSONException {
        JSONStringer jsonStringer = new JSONStringer();
        jsonStringer.object().key("command").value("get_files_by_batch_id_fail").key("config").object()
                .key("batchId").value(batch_id)
                .endObject().endObject();
        return jsonStringer.toString() ;
    }

    public static String getFileByBatchIdOK(String  batch_id) throws JSONException {
        JSONStringer jsonStringer = new JSONStringer();
        jsonStringer.object().key("command").value("get_files_by_batch_id_ok").key("config").object()
                .key("batchId").value(batch_id)
                .endObject().endObject();
        return jsonStringer.toString() ;
    }

    public static String deleteFilesOK(List<String> batchList, List<String> id) throws JSONException {
        JSONStringer jsonStringer = new JSONStringer();
        jsonStringer.object().key("command").value("delete_files_ok").key("config").object()
                .key("userId").value(user_id)
                .key("files").array();

        int len = batchList.size();
        for(int i = 0 ; i < len ; i ++){
            jsonStringer.object()
                    .key("batchId").value(batchList.get(i))
                    .key("id").value(id.get(i))
                    .endObject();
        }
        jsonStringer.endArray().endObject().endObject();
        return jsonStringer.toString();
    }
    public static String deleteFilesFail(List<String> batchList, List<String> fileName ,List<String> filePath) throws JSONException {
        JSONStringer jsonStringer = new JSONStringer();
        jsonStringer.object().key("command").value("delete_files_fail").key("config").object()
                .key("userId").value(user_id)
                .key("files").array();

        int len = batchList.size();
        for(int i = 0 ; i < len ; i ++){
            jsonStringer.object()
                    .key("batchId").value(batchList.get(i))
                    .key("fileName").value(fileName.get(i))
                    .key("path").value(filePath.get(i))
                    .endObject();
        }
        jsonStringer.endArray().endObject().endObject();
        return jsonStringer.toString();
    }

    public static String deleteBatchFile(String batch_id, String status) throws JSONException {
        JSONStringer jsonStringer = new JSONStringer();
        jsonStringer.object().key("command").value("delete_batch_file").key("config").object()
                .key("batchId").value(batch_id)
                .key("userId").value(user_id)
                .key("status").value(status).endObject().endObject();

        return jsonStringer.toString();
    }


    public static void main(String[] args){
    }

}
