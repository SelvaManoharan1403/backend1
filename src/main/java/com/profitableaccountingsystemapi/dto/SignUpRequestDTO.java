package com.profitableaccountingsystemapi.dto;

import lombok.Data;

@Data
public class SignUpRequestDTO {
    private String name;
    private String gender;
    private String emailId;
    private String phoneNumber;
    private String password;
}
