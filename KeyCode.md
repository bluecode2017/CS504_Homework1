# Running-Information-Analysis-Service Application


## 实现步骤

项目开发第一阶段，为实现业务功能，忽略存储方式，先用springboot自带的H2database，来测试业务逻辑的实现。
项目开发第二阶段，业务功能测试通过后，修改后台数据库为mysql， 在pom.xml里增加mysql-connector-java依赖，在application.yml添加 mysql的连接方式；
项目开发第三阶段，集成测试，上线提交

### 1.新建maven project

### 2.修改maven配置文件
修改pom.xml ，加入parent 和dependency 和 build. 其中，spring-boot-starter-parent会加载Spring Boot应用所需的所有默认配置； spring-boot-starter-data-jpa会下载所有Spring Data Jpa所需的依赖； 因为此项目是一个web应用，所以添加spring-boot-starter-web.
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
项目代码结构 
```
-src 
  |--main 
      |--java  
          |--demo
              |--domain  
                   |--RunningInformation (class 实体类)  
                   |--UserInfo (class实体类)   
                   |--RunningInformationRepository (Interface 数据操作DAO) 
              |---restcontroller      
                   |--RunningInformationAnalysisController (class实现requestmapping及部分业务处理)
              |--service 
                   |--Impl  
                         |--RunningInformationServiceImpl (class 实现业务处理逻辑) 
                   |--RunningInformationBulkUploadController (Interface 业务逻辑层) 
             |--RunningInformationBulkUploadController (class 启动入口)
```

### 4.创建主运行程序
设为@SpringBootApplication 
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
domain里的RunningInformation class ,UserInfo class，二者关系目前为为1对1其中userId为自动生成的ID（在数据库中为identity(1,1))，
这两个实体类关系是embeded 和 embedable.
主要代码如下：
```
@Table (name ="private")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@Entity
public class RunningInformation {

    private enum HealthWarningLevel { HIGH,NORMAL,LOW;}



    private String runningId;
    private double totalRunningTime;
    private int heartRate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;


    @Embedded
    private final UserInfo userInfo;

    private HealthWarningLevel healthWarningLevel;

    private double latitude;
    private double longitude;
    private double runningDistance;
    private Date timeStamp;

    public RunningInformation(){

        this.userInfo = null;
    }
    public RunningInformation(final UserInfo userInfo){
        this.userInfo = userInfo;
    }

    @JsonCreator
    public RunningInformation(
            @JsonProperty("runningId") String runningId,
            @JsonProperty("longitude") String longitude,
            @JsonProperty("latitude") String latitude,
            @JsonProperty("runningDistance") String runningDistance,
            @JsonProperty("totalRunningTime") String totalRunningTime,
            @JsonProperty("heartRate") String heartRate,
            @JsonProperty("userInfo") UserInfo userInfo,
            @JsonProperty("timeStamp") String timeStamp) {
        this.runningId = runningId;
        this.longitude = Double.parseDouble(longitude);
        this.latitude = Double.parseDouble(latitude);
        this.runningDistance = Double.parseDouble(runningDistance);
        this.totalRunningTime = Double.parseDouble(totalRunningTime);
        this.heartRate = _getRandomHeartRate(60,200);
        this.timeStamp = new Date();
        this.userInfo = userInfo;
        if(this.heartRate>120){
            this.healthWarningLevel = HealthWarningLevel.HIGH;
        }
        else if(this.heartRate >75){
            this.healthWarningLevel = HealthWarningLevel.NORMAL;
        }
        else if (this.heartRate >=60){
            this.healthWarningLevel = HealthWarningLevel.LOW;
        }else {
            //option 1: Danger
            //option 2: Dintentionally left blank
            //option 3: Exception
            //option 4: Print warning
        }
        System.out.println("check random value ---->"+this.heartRate);
    }

    public String getUsername(){

        return this.userInfo == null ? null : this.userInfo.getUserName();
    }

    public String getAddress(){
        return this.userInfo == null ? null : this.userInfo.getAddress();
    }

    private int _getRandomHeartRate(int min,int max){
        Random rn = new Random();
        return min+rn.nextInt(max-min+1);
    }
}
```

```
@JsonInclude(JsonInclude.Include.NON_NULL)
@Embeddable
@Data
public class UserInfo {
    private String userName;
    private String address;

    public UserInfo() {

    }
    @JsonCreator
    public UserInfo(
            @JsonProperty("username") String userName,
            @JsonProperty("address") String address) {
        this.userName = userName;
        this.address = address;
    }
}
```

### 6.创建Repository接口继承jpaRepository   
项目的RunningInformationRepository接口实现了JpaRepository接口；（实际上JpaRepository实现了PagingAndSortingRepository接口，PagingAndSortingRepository接口实现了CrudRepository接口，CrudRepository接口实现了Repository接口） 因为项目需要返回所有结果，并排序和分页。我调用findAll方法，JpaRepository接口返回的是List, PagingAndSortingRepository和CrudRepository返回的是迭代器；所以我选择JpaRepository接口。

主要代码如下：
```
//@RepositoryRestResource(collectionResourceRel = "runningInformations")
public interface RunningInformationRepository extends JpaRepository<RunningInformation,Long> {

    Page<RunningInformation> findAll(Pageable pageable);

    List<RunningInformation> findByRunningId(@Param("runningId") String runningId);

    void deleteByRunningId(@Param("runningId") String runningId);
}
```
###  7.创建RestController 
RunningInformationAnalysisController，实现requestmaping。根据需求，提供5种功能：

####  /runninginformations ：批量上传数据，关键代码如下：
```
@RequestMapping(value = "/runninginformations", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void bulkUpload(@RequestBody List<RunningInformation> runningInformations) {
        runningInformationService.saveRunningInformation(runningInformations);
    }
```  
```
  @Override
    public List<RunningInformation> saveRunningInformation(List<RunningInformation> runningInformations) {
        return runningInformationRepository.save(runningInformations);
    }
 ```
####  /runninginformations/{runningId}  ： 按RunningID来删除数据 （如果一个RunningID对应多条记录，则删除多条。实现删除多个结果值的功能）,关键代码如下：
```
@RequestMapping(value = "/runninginformations/{runningId}", method = RequestMethod.DELETE)
    public void deleteByRunningId(@PathVariable("runningId") String runningId) {
        runningInformationService.deleteByRunningId(runningId);
    }
```
```
@Override
    public void deleteByRunningId(String runningId) {

        List<RunningInformation> runningInformationList = new ArrayList<RunningInformation>();
        runningInformationList=runningInformationRepository.findByRunningId(runningId);
        for(RunningInformation temp: runningInformationList) {

                runningInformationRepository.delete(temp);

        }
    }
```

#### /runninginformations 列出所有结果，
实现按照healthWarningLevel排序，此处因为healthWarningLevel是枚举类型，且根据heartRate的值得到的枚举值，无法根据枚举值排序，所以改为根据heartRate排序，更好的实现了需求。

实现了根据requirements输出部分属性，有些属性不输出, 有两种方法：

* 第一种是在实体类的属性前面加@JsonIgnore，可以实现输出不显示。但是这种办法无法扩展，限定了应用中所有输出都是如此。
* 第二种，是在restcontroller 这一层，加一些过滤和处理。 获取到rawdata，选择部分属性值，复制到新建的JSONobject里，加以输出，这种办法扩展性比较好。本项目采取这种方式控制输出格式。

关键代码如下：
``` 
   @RequestMapping(value="/runninginformations", method = RequestMethod.GET)
    public ResponseEntity<List<JSONObject>> findAll(@RequestParam(name = "page", defaultValue = kDefaultPage) Integer page,
                                              @RequestParam(name = "size",defaultValue = kDefaultItemPerPage) Integer size){
        Sort sort = new Sort(Sort.Direction.DESC,"heartRate");
        Pageable pageable = new PageRequest(page,size,sort);
        Page<RunningInformation> originResults = runningInformationService.findAll(pageable);
        List<RunningInformation> content =originResults.getContent();
        List<JSONObject> results = new ArrayList<JSONObject>();
        for (RunningInformation item : content){
            JSONObject info = new JSONObject();
            info.put(runningId,item.getRunningId());
            info.put(totalRunningTime ,item.getTotalRunningTime());
            info.put(heartRate ,item.getHeartRate());
            info.put(userId ,item.getId());
            info.put(userName ,item.getUsername());
            info.put(userAddress ,item.getAddress());
            info.put(healthWarningLevel ,item.getHealthWarningLevel());
            results.add(info);
        }
        return new ResponseEntity<List<JSONObject>>(results,HttpStatus.OK);
    }   
``` 

``` 
@Override
    public Page<RunningInformation> findAll(Pageable pageable) {
         return runningInformationRepository.findAll(pageable);
    }
``` 

####  /runninginformations 删除所有数据
``` 
@RequestMapping(value = "/runninginformations", method = RequestMethod.DELETE)
    public void purge() {
        this.runningInformationService.deleteAll();
    }
``` 
####  /runninginformations/ran 上传随机的dummy data，
主要代码为：
```
@RequestMapping(value = "/runninginformations/ran", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveRandomOne() {
        runningInformationService.saveRandomOne(RunningInformation.autoGenerate());
    }
```



end
