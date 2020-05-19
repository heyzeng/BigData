# what is hive

- 官方文档

  The Apache Hive ™ data warehouse software facilitates reading, writing, and managing large datasets residing in distributed storage using SQL. Structure can be projected onto data already in storage. A command line tool and JDBC driver are provided to connect users to Hive

- 释义

  Apache Hive™数据仓库软件通过使用SQL读、写以及管理在分布式存储中的大型数据集。 可以将结构映射到已经存储的数据上。 提供了命令行工具和JDBC驱动程序将用户连接到Hive

- 起源

  由Facebook开源用于解决海量结构化日志的数据统计，是基于Hadoop的一个数据仓库工具，可以将结构化的数据文件映射为一张表，并提供类SQL查询功能，而本质上是将HQL转化为MapReduce

- HQL实现

  - Hive处理的数据存储在HDFS
  - Hive分析数据底层实现为MapReduce
  - 执行程序运行在Yarn上

# why is Hive

- 优点

  - 操作接口采用类SQL语法，提供快速开发的能力（简单、容易上手）
  - 避免了去写MapReduce，减少开发人员的学习成本
  - Hive的执行延迟比较高，因此Hive常用于数据分析，对实时性要求不高的场合。
  - Hive优势在于处理大数据，对于处理小数据没有优势，因为Hive的执行延迟比较高
  - Hive支持用户自定义函数，用户可以根据自己的需求来实现自己的函数

- 缺点

  - Hive的HQL表达能力有限，迭代式算法无法表达，数据挖掘方面不擅长
  - Hive的效率比较低，Hive自动生成的MapReduce作业，通常情况下不够智能化，Hive调优比较困难，粒度较粗

  # Hive架构原理

![Hive架构原理](https://i.loli.net/2020/05/17/KjNfwxvhWnI4C1t.jpg)

- 用户接口

  CLI（hive shell）、JDBC/ODBC(java访问hive)、WEBUI（浏览器访问hive）

- 元数据:Metastore

  元数据包括：表名、表所属的数据库（默认是default）、表的拥有者、列/分区字段、表的类型（是否是外部表）、表的数据所在目录等

- Hadoop

  使用HDFS进行存储，使用MapReduce进行计算

- 驱动器Driver

  - 解析器（SQL Parser）：将SQL字符串转换成抽象语法树AST，这一步一般都用第三方工具库完成，比如antlr；对AST进行语法分析，比如表是否存在、字段是否存在、SQL语义是否有误
  - 编译器（Physical Plan）：将AST编译生成逻辑执行计划
  - 优化器（Query Optimizer）：对逻辑执行计划进行优化。
  - 执行器（Execution）：把逻辑执行计划转换成可以运行的物理计划。对于Hive来说，就是MR/Spark

  # Hive和数据库比较

  由于 Hive 采用了类似SQL 的查询语言 HQL(Hive Query Language)，因此很容易将 Hive 理解为数据库。其实从结构上来看，Hive 和数据库除了拥有类似的查询语言，再无类似之处。本文将从多个方面来阐述 Hive 和数据库的差异。数据库可以用在 Online 的应用中，但是Hive 是为数据仓库而设计的，清楚这一点，有助于从应用角度理解 Hive 的特性。

  - 查询语言

    由于SQL被广泛的应用在数据仓库中，因此，专门针对Hive的特性设计了类SQL的查询语言HQL。熟悉SQL开发的开发者可以很方便的使用Hive进行开发

  - Hive 是建立在 Hadoop 之上的，所有 Hive 的数据都是存储在 HDFS 中的。而数据库则可以将数据保存在块设备或者本地文件系统中

  - 由于Hive是针对数据仓库应用设计的，而数据仓库的内容是读多写少的。因此，Hive中不建议对数据的改写，所有的数据都是在加载的时候确定好的。而数据库中的数据通常是需要经常进行修改的，因此可以使用 INSERT INTO … VALUES 添加数据，使用 UPDATE … SET修改数据

  - Hive在加载数据的过程中不会对数据进行任何处理，甚至不会对数据进行扫描，因此也没有对数据中的某些Key建立索引。Hive要访问数据中满足条件的特定值时，需要暴力扫描整个数据，因此访问延迟较高。由于 MapReduce 的引入， Hive 可以并行访问数据，因此即使没有索引，对于大数据量的访问，Hive 仍然可以体现出优势。数据库中，通常会针对一个或者几个列建立索引，因此对于少量的特定条件的数据的访问，数据库可以有很高的效率，较低的延迟。由于数据的访问延迟较高，决定了 Hive 不适合在线数据查询

  - Hive中大多数查询的执行是通过 Hadoop 提供的 MapReduce 来实现的。而数据库通常有自己的执行引擎

  - Hive 在查询数据的时候，由于没有索引，需要扫描整个表，因此延迟较高。另外一个导致 Hive 执行延迟高的因素是 MapReduce框架。由于MapReduce 本身具有较高的延迟，因此在利用MapReduce 执行Hive查询时，也会有较高的延迟。相对的，数据库的执行延迟较低。当然，这个低是有条件的，即数据规模较小，当数据规模大到超过数据库的处理能力的时候，Hive的并行计算显然能体现出优势

  - 由于Hive是建立在Hadoop之上的，因此Hive的可扩展性是和Hadoop的可扩展性是一致的（世界上最大的Hadoop 集群在 Yahoo!，2009年的规模在4000 台节点左右）。而数据库由于 ACID 语义的严格限制，扩展行非常有限。目前最先进的并行数据库 Oracle 在理论上的扩展能力也只有100台左右
 
  - 由于Hive建立在集群上并可以利用MapReduce进行并行计算，因此可以支持很大规模的数据；对应的，数据库可以支持的数据规模较小