package com.markergus.secondaryserver.dto;

public class IncomingTokenRequestDto {

    private String userId;
    private String deviceId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "IncomingTokenRequestDto{" +
                "userId='" + userId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
