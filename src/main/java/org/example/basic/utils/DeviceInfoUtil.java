package org.example.basic.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.example.basic.dto.DeviceInfo;
import org.springframework.stereotype.Component;

@Component
public class DeviceInfoUtil {
    public static DeviceInfo getDeviceInfo(HttpServletRequest request) {
        String ipv4 = request.getRemoteAddr();
        String device = request.getHeader("User-Agent");
        return new DeviceInfo(ipv4, device);
    }
}
