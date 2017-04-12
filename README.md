此项目为 Running-Information-Analysis-Service

实现的功能需求为：
  请见项目文件“需求说明ProjectRequirements”


实现步骤：

1.新建maven project

2.修改pom ，加入parent 和dependency ，build
其中，spring-boot-starter-parent会加载Spring Boot应用所需的所有默认配置；
spring-boot-starter-data-jpa会下载所有Spring Data Jpa所需的依赖；
添加spring-boot-starter-web是因为我们的工程是一个Web应用.
先用springboot自带的H2database，来测试业务逻辑的实现，测试功能，通过后，再修改为mysql
再在pom.xml里增加mysql-connector-java依赖；

3.创建项目代码结构
-src
 |-main
  |--java
   |--demo
     |--domain
        |--RunningInformation (class 实体类)
        |--UserInfo (class实体类)
		    |--RunningInformationRepository (Interface 数据操作DAO)
    |---restcontroller
        |--RunningInformationBulkUploadController (class)
    |--service
       |--Impl
		      |--RunningInformationServiceImpl (class)
       |--RunningInformationBulkUploadController (Interface 业务逻辑层)
	--RunningInformationBulkUploadController (class 启动入口)


4.创建主运行程序，设为SpringBootApplication，
5.创建实体类，domain里的RunningInformation class ,UserInfo class，二者关系目前为为1对1，设置为embeded 和 embedable
     设置@JsonIgnore 来指定哪些column不返回到前端
5.创建Repository接口继承jpaRepository
   项目的RunningInformationRepository接口实现了JpaRepository接口；（实际上JpaRepository实现了PagingAndSortingRepository接口，PagingAndSortingRepository接口实现了CrudRepository接口，CrudRepository接口实现了Repository接口）
	 因为项目需要返回所有结果，并排序和分页。我调用findAll方法，JpaRepository接口返回的是List, PagingAndSortingRepository和CrudRepository返回的是迭代器；所以我选择JpaRepository接口。	 

6.创建@RestController RunningInformationBulkUploadController，实现requestmaping。


