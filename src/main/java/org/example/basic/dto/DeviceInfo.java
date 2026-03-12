package org.example.basic.dto;

import jakarta.validation.constraints.NotNull;

public record DeviceInfo(@NotNull String ipv4, String device) {
}
