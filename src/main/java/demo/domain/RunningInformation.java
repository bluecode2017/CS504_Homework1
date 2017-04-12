package demo.domain;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Random;

import lombok.Data;

@Table (name ="private")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@Entity
public class RunningInformation {

    private enum HealthWarningLevel { HIGH,NORMAL,LOW;}

    @Id
    @GeneratedValue
    private Long id;

    private String runningId;
    private double totalRunningTime;
    private int heartRate;
    @Embedded
    private final UserInfo userInfo;

    private HealthWarningLevel healthWarningLevel;

    @JsonIgnore
    private double latitude;
    @JsonIgnore
    private double longitude;
    @JsonIgnore
    private double runningDistance;
    @JsonIgnore
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

        System.out.println(this.heartRate);
    }

    public Long getUserId(){
        return this.userInfo == null ? null : this.userInfo.getUserId();
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
