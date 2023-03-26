package com.markergus.mainserver.dto;

import lombok.ToString;

@ToString
public class CustomerCreationDto {

    private String fullName;

    private String userId;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
