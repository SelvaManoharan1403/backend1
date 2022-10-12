package com.profitableaccountingsystemapi.dto;

import lombok.Data;

@Data
public class TokenDTO {
    private String refreshToken;
    private String id;
}
