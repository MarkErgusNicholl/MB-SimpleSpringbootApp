package com.markergus.mainserver.dto;

import lombok.ToString;

@ToString
public class CustomerUpdateDto {

    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
