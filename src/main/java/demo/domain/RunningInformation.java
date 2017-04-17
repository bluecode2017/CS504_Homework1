package demo.domain;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Random;

import lombok.Data;

@Table(name = "private")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@Entity
public class RunningInformation {

    private enum HealthWarningLevel {HIGH, NORMAL, LOW, Dangerous;}

    private final static String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

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

    public RunningInformation() {

        this.userInfo = null;
    }

    public RunningInformation(final UserInfo userInfo) {
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
        this.heartRate = _getRandomNumber(60, 200);
        this.timeStamp = new Date();
        this.userInfo = userInfo;
        if (this.heartRate > 120) {
            this.healthWarningLevel = HealthWarningLevel.HIGH;
        } else if (this.heartRate > 75) {
            this.healthWarningLevel = HealthWarningLevel.NORMAL;
        } else if (this.heartRate >= 60) {
            this.healthWarningLevel = HealthWarningLevel.LOW;
        } else {
            this.healthWarningLevel = HealthWarningLevel.Dangerous;
            //option 1: Danger
            //option 2: Dintentionally left blank
            //option 3: Exception
            //option 4: Print warning
        }
        System.out.println("check random value ---->" + this.heartRate);
    }

    public static RunningInformation autoGenerate() {
        UserInfo userInfo = new UserInfo(_generateUsername(), "504 CS Street, Mountain View, CA 88888");
        RunningInformation runningInformation = new RunningInformation(userInfo);
        runningInformation.setRunningId(_generateRunningId());
        runningInformation.setLatitude(39.927434);
        runningInformation.setLongitude(-76.635816);
        runningInformation.setRunningDistance(_getRandomNumber(100, 5000));
        runningInformation.setTimeStamp(new Date());
        runningInformation.setTotalRunningTime(_getRandomNumber(500, 5000));
        int heartRate = _getRandomNumber(60, 200);
        runningInformation.setHeartRate(heartRate);

        if (heartRate > 120) {
            runningInformation.setHealthWarningLevel(HealthWarningLevel.HIGH);
        } else if (heartRate > 75) {
            runningInformation.setHealthWarningLevel(HealthWarningLevel.NORMAL);
        } else if (heartRate >= 60) {
            runningInformation.setHealthWarningLevel(HealthWarningLevel.LOW);
        } else {
            runningInformation.setHealthWarningLevel(HealthWarningLevel.Dangerous);
        }
        return runningInformation;
    }

    public String getUsername() {

        return this.userInfo == null ? null : this.userInfo.getUserName();
    }

    public String getAddress() {
        return this.userInfo == null ? null : this.userInfo.getAddress();
    }

    public void setHeartRate() {
        if (this.heartRate == 0) {
            if (this.heartRate > 120) {
                this.healthWarningLevel = HealthWarningLevel.HIGH;
            } else if (this.heartRate > 75) {
                this.healthWarningLevel = HealthWarningLevel.NORMAL;
            } else if (this.heartRate >= 60) {
                this.healthWarningLevel = HealthWarningLevel.LOW;
            } else {
                //option 1: Danger
                //option 2: Dintentionally left blank
                //option 3: Exception
                //option 4: Print warning
            }
        }
    }

    private static int _getRandomNumber(int min, int max) {
        Random rn = new Random();
        return min + rn.nextInt(max - min + 1);
    }

    private static String _generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(allChar.charAt(random.nextInt(allChar.length())));
        }
        return sb.toString();
    }

    private static String _generateRunningId() {
        return _generateString(8) + "-" + _generateString(4) + "-" + _generateString(4) + "-" + _generateString(4) + "-" + _generateString(12);
    }

    private static String _generateUsername() {
        return _generateString(5);
    }

}
