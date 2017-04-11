package demo.domain;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.PersistenceConstructor;
import lombok.Data;

@Table (name ="private")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@RequiredArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@Data
public class RunningInformation {
    @Id
    private String runningId;
    private double totalRunningTime;
    private int heartRate;
    @Embedded
    private  UserInfo userInfo;
    private String healthWarningLevel;

    @JsonIgnore
    private double latitude;
    @JsonIgnore
    private double longitude;
    @JsonIgnore
    private long runningDistance;

    @JsonIgnore
    private Date timeStamp;

    public RunningInformation(UserInfo userInfo){
        this.userInfo = userInfo;

    }

    @JsonCreator
    public RunningInformation(@JsonProperty("heartRate") int heartRate) {
        if(heartRate == 0){
            Random rand = new Random();
            this.heartRate = rand.nextInt(200 - 60 + 1) + 60;
        }else {
            this.heartRate = heartRate;
        }


        if ( this.heartRate >= 60 && this.heartRate <= 75 )
            this.healthWarningLevel = "LOW";
        if ( this.heartRate > 75 && this.heartRate <= 120 )
            this.healthWarningLevel = "NORMAL";
        if ( this.heartRate > 120  )
            this.healthWarningLevel =  "HIGH";
    }
    /*
    public RunningInformation() {
        this.userInfo = null;
    }
*/
    @JsonCreator
    public RunningInformation(String runningId, double latitude, double totalRunningTime,double longitude, long runningDistance, int heartRate, Date timeStamp, UserInfo userInfo) {
        this.runningId = runningId;
        this.userInfo = userInfo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.totalRunningTime = totalRunningTime;
        this.runningDistance = runningDistance;
        this.heartRate = heartRate;
        this.timeStamp = timeStamp;

        Random rand = new Random();
        this.heartRate = rand.nextInt(200 - 60 + 1) + 60;


        if ( this.heartRate >= 60 && this.heartRate <= 75 )
            this.healthWarningLevel = "LOW";
        if ( this.heartRate > 75 && this.heartRate <= 120 )
            this.healthWarningLevel = "NORMAL";
        if ( this.heartRate > 120  )
            this.healthWarningLevel =  "HIGH";
    }
  /*
    public String gethealthWarningLevel(){
        if ( this.heartRate >= 60 && this.heartRate <= 75 )
            return "LOW";
        if ( this.heartRate > 75 && this.heartRate <= 120 )
            return "NORMAL";
        if ( this.heartRate > 120  )
            return "HIGH";
    }
    */
}
