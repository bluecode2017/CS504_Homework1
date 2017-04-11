package demo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    private String userName;
    private String address;

    public UserInfo() {

    }

    public UserInfo(long userId,String userName, String address) {
        this.userId = userId;
        this.userName = userName;
        this.address = address;
    }
}
