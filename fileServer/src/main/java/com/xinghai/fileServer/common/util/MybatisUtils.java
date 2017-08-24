package com.xinghai.fileServer.common.util;

import com.xinghai.fileServer.common.exception.FileServerErrorEnum;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by scream on 2017/7/18.
 */
public class MybatisUtils {
    static  final  Logger logger = Logger.getLogger(MybatisUtils.class);
    private static final String CONFIG_PATH = "mybatis-config.xml";
    private static SqlSessionFactory factory;
    private static TransactionFactory transactionFactory = new JdbcTransactionFactory();

    static {
        try {
            try (InputStream inputStream = MybatisUtils.class.getClassLoader().getResourceAsStream(CONFIG_PATH)) {
                factory = new SqlSessionFactoryBuilder().build(inputStream);
            }
        } catch (IOException e) {
            logger.error("mybatis-config.xml not found", e);
        }
    }

    public static SqlSession openSession(boolean autoCommit) {
        return factory.openSession(autoCommit);
    }

    public static SqlSession openSession() {
        return openSession(true);
    }

    public static Transaction openTransaction(final SqlSession session) {
        return transactionFactory.newTransaction(session.getConnection());
    }

    //多次操作在同一个session中提交
    public static FileServerErrorEnum commitReturn(final SqlSession session,FileServerErrorEnum fileServerErrorEnum){
        session.commit();
        return fileServerErrorEnum;
    }
}
