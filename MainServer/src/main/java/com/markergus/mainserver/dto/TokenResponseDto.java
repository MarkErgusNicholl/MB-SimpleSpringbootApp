package com.markergus.mainserver.dto;

import lombok.ToString;

@ToString
public class TokenResponseDto {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
