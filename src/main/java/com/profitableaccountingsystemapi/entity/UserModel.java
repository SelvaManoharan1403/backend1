package com.profitableaccountingsystemapi.entity;

import com.profitableaccountingsystemapi.common.Constant;
import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;

@Data
public class UserModel {
    @Id
    private String id;
    private String name;
    private String gender;
    private String emailId;
    private String phoneNumber;
    private String userType = Constant.USER_TYPE.NORMAL;
    private String password;
    private Boolean isActive = true;
    private Integer loginCount = 0;
    private String ssoType;
    private DateTime loginAt;
    private DateTime createAt;
    private DateTime updateAt;
    private String resetToken;

}
