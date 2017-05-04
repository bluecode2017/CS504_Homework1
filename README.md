# Running-Information-Analysis-Service Application

## 主要功能

* 此项目为 RunningTrackingProject的其中一个backend的service组件, 采用SpringBoot+SpringData+mysql 技术实现了RunningInformation数据上传 和 主要数据的查询。
* 具体功能需求请参见当前目录下的《ProjectRequirements》
* **增加了一项扩展功能： 可以上传一条random 的dummy data，不需要手工准备数据.**

## 下载、启动应用

1. 下载代码
```
git clone https://github.com/bluecode2017/Running-Information-Analysis-Service.git
cd Running-Information-Analysis-Service
```

2. 启动数据库
```
docker-compose up -d
```
初次运行项目之前要先创建数据库
```
mysql --host=127.0.0.1 --port=3306 --user=root --password=root

mysql> show databases;
mysql> create database running_information_analysis_db;
```
3. 编译源程序
```
mvn clean install
```

4. 启动server
```
java -jar ./target/Running-Information-Analysis-Service-1.0.0.BUILD-SNAPSHOT.jar
```
5. 上传数据
```
./upload-running-informations.sh

```

## 使用方法
打开postman插件
```
输入GET request 获取 所有数据 
localhost:8080/runninginformations

输入DELETE request， 按RunningID 删除数据
localhost:8080/runninginformations/07e8db69-99f2-4fe2-b65a-52fbbdf8c32c

输入DEELETE request 
localhost:8080/runninginformations

输入POST request ，此处将source data 贴在Body，并选择Json格式
localhost:8080/runninginformations     

输入POST request loadlhost:8080/runninginformations/ran
```

同时，可以访问mysql数据库来查看private表里数据的变化.
```
mysql --host=127.0.0.1 --port=3306 --user=root --password=root

mysql> show databases;
mysql> use running_information_analysis_db;
mysql> select * from private;
```

## 版本说明

* Version 1.0  完成业务功能，忽略存储方式，用springboot自带的H2database，来实现业务逻辑。

* Version 2.0  业务功能测试通过后，修改后台数据库为mysql， 在pom.xml里增加mysql-connector-java依赖，在application.yml添加 mysql的连接方式；

* Version 2.2  添加扩展功能。

* Version 3.0  改进，更改实体关系为1对n。 改进存储方式。【TODO】

## 项目环境

* 开发环境：Ubuntu OS Virtual Machine + Java JDK 1.8  
* 主要技术：Maven + SpringBoot 1.3.0 + Spring Data + Lombok + MySQL 5.7 + MysqlClient + PostMan
* 开发工具：Intellij IDEA + Bash Shell

* 项目结构
 
```
-src 
  |--main 
      |--java  
          |--demo
              |--domain  
                   |--RunningInformation (class  实体类)  
                   |--UserInfo (class实体类)   
                   |--RunningInformationRepository (Interface  数据操作DAO) 
              |---restcontroller      
                   |--RunningInformationAnalysisController (class  实现requestmapping及部分业务处理)
              |--service 
                   |--Impl  
                         |--RunningInformationServiceImpl (class  实现业务处理逻辑) 
                   |--RunningInformationBulkUploadController (Interface   业务逻辑层) 
             |--RunningInformationBulkUploadController (class   启动入口)
```

## 项目输入输出

输入数据为JSON格式的源数据，格式如下：
```
[
  {
    "runningId": "7c08973d-bed4-4cbd-9c28-9282a02a6032",
    "latitude": "38.9093216",
    "longitude": "-77.0036435",
    "runningDistance": "39492",
    "totalRunningTime": "2139.25",
    "heartRate": 0,
    "timestamp": "2017-04-01T18:50:35Z",
    "userInfo": {
      "username": "ross0",
      "address": "504 CS Street, Mountain View, CA 88888"
    }
  },
  {
    "runningId": "07e8db69-99f2-4fe2-b65a-52fbbdf8c32c",
    "latitude": "39.927434",
    "longitude": "-76.635816",
    "runningDistance": "1235",
    "totalRunningTime": "3011.23",
    "heartRate": 0,
    "timestamp": "2017-04-01T18:50:35Z",
    "userInfo": {
      "username": "ross1",
      "address": "504 CS Street, Mountain View, CA 88888"
    }
  }
  ]
  ```
  
输入方式有三种:

* 第一种使用 网页request POST /runninginformations 方式，需在Body处准备好源数据，可上传1个或多个Json数据，方便临时添加测试数据.

* 第二种，编写shell，在应用启动后，在terminal执行./upload-running-informations.sh 可上传大批量数据。关键代码如下：
 ```
 #!/usr/bin/env bash
curl -H "Content-type: application/json" localhost:8080/runninginformations  -d @runningInformations.json
```
* 第三种： 随机上传数据，提交网页request /runninginformations/ran 方式，不需要Body处准备数据。方便临时添加测试数据。

因为使用了RESTcontroller，数据的存取都通过 http request 完成。
* https://localhost:8080/runninginformations ：批量上传数据
* https://localhost:8080/runninginformations ：删除所有数据
* https://localhost:8080/runninginformations/{runningId}  ： 按RunningID来删除相应数据
* https://localhost:8080/runninginformations 列出所有结果（ 返回结果根据healthWarningLevel从高到底进行排序，默认显示第一页，每页2个数据，并根据requirements进行删选，有些属性不输出）.
* https://localhost:8080/runninginformations/ran 上传一条数据，random随机生成runningID和userName。

输出为JSON response，格式如下：
```
[
  {
    "userAddress": "504 CS Street, Mountain View, CA 88888",
    "healthWarningLevel": "HIGH",
    "heartRate": 196,
    "runningId": "35be446c-9ed1-4e3c-a400-ee59bd0b6872",
    "totalRunningTime": 0,
    "userName": "ross5",
    "userId": 70
  },
  {
    "userAddress": "504 CS Street, Mountain View, CA 88888",
    "healthWarningLevel": "HIGH",
    "heartRate": 191,
    "runningId": "2f3c321b-d239-43d6-8fe0-c035ecdff232",
    "totalRunningTime": 85431.23,
    "userName": "ross2",
    "userId": 67
  }
]
```

## 项目开发说明

### 1.新建maven project

### 2.修改maven配置文件
修改pom.xml ，加入parent 和dependency 和 build. 
其中，spring-boot-starter-parent会加载Spring Boot应用所需的所有默认配置； spring-boot-starter-data-jpa会下载所有Spring Data Jpa所需的依赖； 因为此项目是一个web应用，所以添加spring-boot-starter-web. 引用json-simple 为创建Json 对象. 引用mysql-connector-java, 实现Mysql数据存取. 引用spring-boot-starter-data-rest 实现RESTAPI.
```
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-rest</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-rest-hal-browser</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.16.12</version>
        </dependency>        
        <dependency>
            <groupId>com.googlecode.json-simple</groupId>
            <artifactId>json-simple</artifactId>
            <version>1.1.1</version>
        </dependency>
    </dependencies>
```

### 3.创建项目结构

### 4.创建主运行程序
设为@SpringBootApplication 指定此应用为一个spring boot 应用。
创建程序配置文件 application.yml
```
Server:
   port: 8080
spring:
   application:
       name: running-information-analysis-service
   datasource:
      url: jdbc:mysql://localhost:3306/running_information_analysis_db
      username: root
      password: root
   jpa:
      hibernate:
         ddl-auto: update
```

### 5.创建实体类
domain里的RunningInformation class ,UserInfo class，二者逻辑关系目前为为1对1，一个user 只有一条 running information， 其中userId为自动生成的ID (类似数据库中为identity(1,1)). 这两个实体类关系是embeded 和 embedable. @Table 执行存储在后台数据库名叫private的表里。程序运行后，如果数据库里没有此表，则会自动创建，如果已有，则会直接使用。


### 6.创建Repository接口继承jpaRepository   

## TODO Plan
Update Entity class UserInfo 和 RunningInformation, change their relationship to 1：n， and save into two seperate table in Mysql。

## LICENSE
[Apache](https://github.com/bluecode2017/CS504_Homework1/blob/master/LICENSE)




