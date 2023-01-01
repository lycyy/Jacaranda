package com.example.service.Service;

import org.springframework.stereotype.Service;

@Service
public interface RedisService {
    void set(String key , String value);
    void setJacarandaCode(String key, String value);
    void setfb(String key, String value);

    String get(String key);

    void remove(String key);

    public boolean hasKey(String key);

    public boolean set(String key, Object value);
}

