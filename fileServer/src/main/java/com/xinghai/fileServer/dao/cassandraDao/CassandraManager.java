package com.xinghai.fileServer.dao.cassandraDao;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.xinghai.fileServer.config.FileServerConfig;

/**
 * Created by scream on 2017/7/26.
 */
public class CassandraManager {
    static public Session session = null;
    static public Cluster cluster = null;
    static public Session getSession(){
        if(session == null){
            synchronized (CassandraManager.class){
                if (session == null){
                    close();
                    init();
                }
            }
        }
        return  session;
    }

    public static void init() {
        if (session == null) {
            SocketOptions opts = new SocketOptions();
            // The default 5 seconds is too small.
            opts.setConnectTimeoutMillis(30000);
            opts.setReadTimeoutMillis(30000);
            opts.setKeepAlive(true);
            cluster = Cluster.builder().withSocketOptions(opts).addContactPoint(FileServerConfig.CASSANDRA_IP.get(0)).build();
            session = cluster.connect();
        }
    }

    static public void close(){
        if(session != null){
            session.close();
            session = null;
        }
        if(cluster != null){
            cluster.close();
            cluster = null;
        }
    }
}
