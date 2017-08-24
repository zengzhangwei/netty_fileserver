package com.xinghai.fileServer.dao.cassandraDao;

import com.datastax.driver.core.Session;

/**
 * Created by scream on 2017/7/24.
 * cassandra创建表基本类
 */
public class CreateKeySpaceAndTable {
        public static void main(String args[]){
            //Query
            String query = "CREATE KEYSPACE file WITH replication "
                    + "= {'class':'SimpleStrategy', 'replication_factor':1};";

            //Creating Session object
            Session session = CassandraManager.getSession();

            //Executing the query
            session.execute(query);

            //using the KeySpace
            session.execute("USE file");
            System.out.println("Keyspace created");

            //
            String createTable = "create table file.file_chunk(\n" +
                    "          file_id uuid,\n" +
                    "          block_count int,\n" +
                    "          content blob,\n" +
                    "          primary key(file_id,block_count)\n" +
                    "          );";
            session.execute(createTable);
            System.out.println("Table created");
            CassandraManager.close();

        }
}