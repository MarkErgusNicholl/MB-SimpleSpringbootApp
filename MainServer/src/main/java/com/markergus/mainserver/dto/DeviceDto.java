package com.markergus.mainserver.dto;

import lombok.ToString;

@ToString
public class DeviceDto {

    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
