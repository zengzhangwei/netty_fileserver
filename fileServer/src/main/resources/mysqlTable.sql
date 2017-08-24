/*记录文件详细信息*/
CREATE TABLE file_meta(
   id bigint(32) NOT NULL AUTO_INCREMENT,    /*记录id*/
   parent_id bigint(32) DEFAULT NULL,        /*父id*/
   file_id  char(36) DEFAULT NULL,           /*文件id*/
   block_count int(32) DEFAULT NULL,         /*文件块数*/
   created_by varchar(50) DEFAULT NULL,      /*创建人*/
   created_on datetime DEFAULT NULL,         /*创建时间*/
   modified_by varchar(50) DEFAULT NULL,     /*修改人*/
   modified_on datetime DEFAULT NULL,        /*修改时间*/
   name varchar(250) DEFAULT NULL,            /*文件名*/
   size bigint(64) DEFAULT NULL,              /*文件大小*/
   content_type varchar(100) DEFAULT NULL,   /*文件类型*/
   hash char(32) DEFAULT NULL,                /*hash值*/
   type enum('1','2') DEFAULT NULL,          /*类型，文件夹为1，文件为2*/
   storage_source varchar(50) DEFAULT NULL, /*存储源，例如hdfs，cassandra*/
   PRIMARY KEY (id),
   UNIQUE KEY unique_file (parent_id,type,name) /*保证每一文件夹下不具同名文件或者文件夹*/
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8