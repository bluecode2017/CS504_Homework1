package demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.javafx.beans.IDProperty;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
