package com.profitableaccountingsystemapi.dto;

import lombok.Data;

@Data
public class UpdatePasswordDTO {
    private String password;
    private String resetToken;

}
