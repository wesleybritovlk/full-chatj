package com.github.wesleybritovlk.fullchatj.core;

import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonResource {
    public static Map<String, Object> toMessage(String message) {
        return Map.of("message", message);
    }

    public static Map<String, Object> toMessage(String message, Object data) {
        return Map.of("message", message, "data", data);
    }

    public static Map<String, Object> toData(Object data) {
        return Map.of("data", data);
    }

}