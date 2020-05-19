# Hive安装地址
- [Hive官网地址](http://hive.apache.org/)
- [文档查看地址](https://cwiki.apache.org/confluence/display/Hive/GettingStarted)
- [下载地址](http://archive.apache.org/dist/hive/)

# Hive安装部署
## Hive安装及配置
- 把apache-hive-1.2.1-bin.tar.gz上传到linux的/opt/software目录下
- 解压apache-hive-1.2.1-bin.tar.gz到/opt/module/目录下面
```
[root@hadoop102 software]$ tar -zxvf apache-hive-1.2.1-bin.tar.gz -C /opt/module/
```
- 修改apache-hive-1.2.1-bin.tar.gz的名称为hive
```
[root@hadoop102 module]$ mv apache-hive-1.2.1-bin/ hive
```
- 修改/opt/module/hive/conf目录下的hive-env.sh.template名称为hive-env.sh
```
[root@hadoop102 conf]$ mv hive-env.sh.template hive-env.sh
```
- 配置hive-env.sh文件
```bash
# 配置HADOOP_HOME路径E
export HADOOP_HOME=/opt/module/hadoop-2.7.2
# 配置HIVE_CONF_DIR路径
export HIVE_CONF_DIR=/opt/module/hive/conf
```
## Hadoop集群配置
- 必须启动hdfs和yarn
```bash
[root@hadoop102 hadoop-2.7.2]$ sbin/start-dfs.sh
[root@hadoop103 hadoop-2.7.2]$ sbin/start-yarn.sh
```
- 在HDFS上创建/tmp和/user/hive/warehouse两个目录并修改他们的同组权限可写
```
[root@hadoop102 hadoop-2.7.2]$ bin/hadoop fs -mkdir /tmp
[root@hadoop102 hadoop-2.7.2]$ bin/hadoop fs -mkdir -p /user/hive/warehouse
[root@hadoop102 hadoop-2.7.2]$ bin/hadoop fs -chmod g+w /tmp
[root@hadoop102 hadoop-2.7.2]$ bin/hadoop fs -chmod g+w /user/hive/warehouse
```
## Hive基本操作
- 启动hive
```
[root@hadoop102 hive]$ bin/hive
```
- 查看数据库
```
hive> show databases;
```
- 打开默认数据库
```
hive> use default;
```
- 显示default数据库中的表
```
hive> show tables;
```
- 创建一张表
```
hive> create table student(id int, name string);
```
- 查看表的结构
```
hive> desc student;
hive> show create table student;
```
- 向表中插入数据
```
hive> insert into student values(1000,"ss");
```
- 查询表中数据
```
hive> select * from student;
```
- 退出hive
```
hive> quit;
```
# 将本地文件导入Hive案例
- 需求 

将本地/opt/module/datas/student.txt这个目录下的数据导入到hive的student(id int, name string)表中。
- 数据准备
    - 在/opt/module/目录下创建datas
    ```
    [root@hadoop102 module]$ mkdir datas
    ```
    - 在/opt/module/datas/目录下创建student.txt文件并添加数据
    ```
    [root@hadoop102 datas]$ touch student.txt
    [root@hadoop102 datas]$ vi student.txt
    1001	zhangshan
    1002	lishi
    1003	zhaoliu
    ```
- Hive实际操作
    - 创建student表, 并声明文件分隔符’\t’
    ```
    hive> create table student(id int, name string) ROW FORMAT DELIMITED FIELDS TERMINATED
    BY '\t';
    ```
    - 加载/opt/module/datas/student.txt 文件到student数据库表中
    ```
    load data local inpath '/opt/module/datas/student.txt' into table student;
    ```
    - Hive查询结果
    ```
    hive> select * from student;
    OK
    1001	zhangshan
    1002	lishi
    1003	zhaoliu
    Time taken: 0.266 seconds, Fetched: 3 row(s)
    ```
- 遇到的问题

    - 再打开一个客户端窗口启动hive，会产生java.sql.SQLException异常
    - 原因是，Metastore默认存储在自带的derby数据库中，推荐使用MySQL存储Metastore;
```
Exception in thread "main" java.lang.RuntimeException: java.lang.RuntimeException:
 Unable to instantiate
 org.apache.hadoop.hive.ql.metadata.SessionHiveMetaStoreClient
        at org.apache.hadoop.hive.ql.session.SessionState.start(SessionState.java:522)
        at org.apache.hadoop.hive.cli.CliDriver.run(CliDriver.java:677)
        at org.apache.hadoop.hive.cli.CliDriver.main(CliDriver.java:621)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:606)
        at org.apache.hadoop.util.RunJar.run(RunJar.java:221)
        at org.apache.hadoop.util.RunJar.main(RunJar.java:136)
Caused by: java.lang.RuntimeException: Unable to instantiate org.apache.hadoop.hive.ql.metadata.SessionHiveMetaStoreClient
        at org.apache.hadoop.hive.metastore.MetaStoreUtils.newInstance(MetaStoreUtils.java:1523)
        at org.apache.hadoop.hive.metastore.RetryingMetaStoreClient.<init>(RetryingMetaStoreClient.java:86)
        at org.apache.hadoop.hive.metastore.RetryingMetaStoreClient.getProxy(RetryingMetaStoreClient.java:132)
        at org.apache.hadoop.hive.metastore.RetryingMetaStoreClient.getProxy(RetryingMetaStoreClient.java:104)
        at org.apache.hadoop.hive.ql.metadata.Hive.createMetaStoreClient(Hive.java:3005)
        at org.apache.hadoop.hive.ql.metadata.Hive.getMSC(Hive.java:3024)
        at org.apache.hadoop.hive.ql.session.SessionState.start(SessionState.java:503)
... 8 more
```
# Hive元数据配置到MySQL
## 驱动拷贝
- 拷贝/opt/software/mysql-libs/mysql-connector-java-5.1.27目录下的mysql-connector-java-5.1.27-bin.jar到/opt/module/hive/lib/
```
[root@hadoop102 mysql-connector-java-5.1.27]# cp mysql-connector-java-5.1.27-bin.jar
 /opt/module/hive/lib/
```
## 配置Metastore到MySQL
- 在/opt/module/hive/conf目录下创建一个hive-site.xml
```
[root@hadoop102 conf]$ touch hive-site.xml
[root@hadoop102 conf]$ vi hive-site.xml
```
- 根据官方文档配置参数，拷贝数据到hive-site.xml文件中
```
<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
	<property>
	  <name>javax.jdo.option.ConnectionURL</name>
	  <value>jdbc:mysql://hadoop102:3306/metastore?createDatabaseIfNotExist=true</value>
	  <description>JDBC connect string for a JDBC metastore</description>
	</property>

	<property>
	  <name>javax.jdo.option.ConnectionDriverName</name>
	  <value>com.mysql.jdbc.Driver</value>
	  <description>Driver class name for a JDBC metastore</description>
	</property>

	<property>
	  <name>javax.jdo.option.ConnectionUserName</name>
	  <value>root</value>
	  <description>username to use against metastore database</description>
	</property>

	<property>
	  <name>javax.jdo.option.ConnectionPassword</name>
	  <value>000000</value>
	  <description>password to use against metastore database</description>
	</property>
</configuration>
```
- 配置完毕后，如果启动hive异常，可以重新启动虚拟机。（重启后，别忘了启动hadoop集群

## 多窗口启动Hive测试
- 先启动MySQL
```
[root@hadoop102 mysql-libs]$ mysql -uroot -p000000
```
- 查看有几个数据库
```
mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql             |
| performance_schema |
| test               |
+--------------------+
```
- 再次打开多个窗口，分别启动hive
```
[root@hadoop102 hive]$ bin/hive
```
- 启动hive后，回到MySQL窗口查看数据库，显示增加了metastore数据库
```
mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| metastore          |
| mysql             |
| performance_schema |
| test               |
+--------------------+
```
## HiveJDBC访问
- 启动hiveserver2服务
```
[root@hadoop102 hive]$ bin/hiveserver2
```
- 启动beeline
```
[root@hadoop102 hive]$ bin/beeline
Beeline version 1.2.1 by Apache Hive
beeline>
```
- 连接hiveserver2
```
beeline> !connect jdbc:hive2://hadoop102:10000（回车）
Connecting to jdbc:hive2://hadoop102:10000
Enter username for jdbc:hive2://hadoop102:10000: root（回车）
Enter password for jdbc:hive2://hadoop102:10000: （直接回车）
Connected to: Apache Hive (version 1.2.1)
Driver: Hive JDBC (version 1.2.1)
Transaction isolation: TRANSACTION_REPEATABLE_READ
0: jdbc:hive2://hadoop102:10000> show databases;
+----------------+--+
| database_name  |
+----------------+--+
| default        |
| hive_db2       |
+----------------+--+
```
## Hive常用交互命令
```
[root@hadoop102 hive]$ bin/hive -help
usage: hive
 -d,--define <key=value>          Variable subsitution to apply to hive
                                  commands. e.g. -d A=B or --define A=B
    --database <databasename>     Specify the database to use
 -e <quoted-query-string>         SQL from command line
 -f <filename>                    SQL from files
 -H,--help                        Print help information
    --hiveconf <property=value>   Use value for given property
    --hivevar <key=value>         Variable subsitution to apply to hive
                                  commands. e.g. --hivevar A=B
 -i <filename>                    Initialization SQL file
 -S,--silent                      Silent mode in interactive shell
 -v,--verbose                     Verbose mode (echo executed SQL to the console)
```
- "-e"不进入hive的交互窗口执行sql语句
```
[root@hadoop102 hive]$ bin/hive -e "select id from student;"
```
- "-f"执行脚本中sql语句
    - 在/opt/module/datas目录下创建hivef.sql文件
    ```
    [root@hadoop102 datas]$ touch hivef.sql
    ```
    - 执行文件中的sql语句
    ```
    [root@hadoop102 hive]$ bin/hive -f /opt/module/datas/hivef.sql
    ```
    - 执行文件中的sql语句并将结果写入文件中
    ```
    [root@hadoop102 hive]$ bin/hive -f /opt/module/datas/hivef.sql  > /opt/module/datas/hive_result.txt
    ```
## Hive其他命令操作
- 退出hive窗口：
```
hive(default)>exit;
hive(default)>quit;
```
在新版的hive中没区别了，在以前的版本是有的：
exit:先隐性提交数据，再退出；
quit:不提交数据，退出；
- 在hive cli命令窗口中如何查看hdfs文件系统
```
hive(default)>dfs -ls /
```
- 在hive cli命令窗口中如何查看本地文件系统
```
hive(default)>! ls /opt/module/datas
```
- 查看在hive中输入的所有历史命令
```
[root@hadoop102 ~]$ cat .hivehistory
```
## 参数配置方式
- 查看当前所有的配置信息
```
hive>set;
```
### 参数的配置三种方式
- 配置文件方式
    - 默认配置文件：hive-default.xml,
    - 用户自定义配置文件：hive-site.xml
    - 注意：用户自定义配置会覆盖默认配置。
另外，Hive也会读入Hadoop的配置，因为Hive是作为Hadoop的客户端启动的，Hive的配置会覆盖Hadoop的配置。配置文件的设定对本机启动的所有Hive进程都有效
### 命令行参数方式
启动Hive时，可以在命令行添加-hiveconf param=value来设定参数。
例如：
```
[root@hadoop103 hive]$ bin/hive -hiveconf mapred.reduce.tasks=10;
```
注意：仅对本次hive启动有效
- 查看参数设置：
```
hive (default)> set mapred.reduce.tasks;
```
### 参数声明方式
可以在HQL中使用SET关键字设定参数
例如：
```
hive (default)> set mapred.reduce.tasks=100;
```
注意：仅对本次hive启动有效。
- 查看参数设置
```
hive (default)> set mapred.reduce.tasks;
```
上述三种设定方式的优先级依次递增。即配置文件<命令行参数<参数声明。注意某些系统级的参数，例如log4j相关的设定，必须用前两种方式设定，因为那些参数的读取在会话建立以前已经完成了。






    
    
    


