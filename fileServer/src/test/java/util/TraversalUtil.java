package util;

import com.xinghai.fileServer.domain.PO.FileMeta;

import java.util.Iterator;
import java.util.List;

/**
 * Created by scream on 2017/8/3.
 */
public class TraversalUtil {
    public static  void traversalList(List<FileMeta> list){
        Iterator<FileMeta> it = list.iterator();
        while(it.hasNext()){
           FileMeta fileMeta = it.next();
           System.out.println(fileMeta.toString());
        }
    }
}
