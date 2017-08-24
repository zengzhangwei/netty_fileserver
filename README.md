版本：1.0 <br>
实现了接收http get，post请求，文件存储到服务器某一文件夹下，文件存进cassandra，维持文件树形结构。<br>

#### 1. 配置文件介绍：
- config.json:服务中参数配置，包括文件在服务端文件夹，线程数等参数，其对应代码中fileServer\src\main\java\com\xinghai\fileServer\config\ConfigurationMatchJSON.java文件，该类对配置字段有详细解释。
- mybatis-config.xml：关于mysql的配置文件
- mysqlTable.sql:mysql 初始化脚本。
- log4j.properties: 日志配置文件

#### 2.项目包介绍
- common:定义异常，util类，枚举类。
- config: 项目配置类
- dao: 关于访问mysql和cassandra的类
- domain: 各种参数对象和持久层对象
- fileHandle:暴露的各种存储接口以及相关数据处理类
- service：netty接收http请求处理类

#### 3. 服务启动步骤
1. 修改config.json中cassandra ip列表和fileDir,tempFileDir 字段，其中fileDir是文件在服务端的位置，tempFileDi是临时文件的存储位置。
2. 将mysqlTable.sql脚本执行，并在mybatis-config.xml中修改ip和数据库用户名密码。
3. 执行fileServer\src\main\java\com\xinghai\fileServer\dao\cassandraDao\CreateKeySpaceAndTable.java 对cassandra进行相关初始化工作。
4. 执行fileServer\src\main\java\com\xinghai\fileServer\service\nettyInit\HttpFileServer.java 启动服务。
