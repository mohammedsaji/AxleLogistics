package com.app.logistics.auth.authUtils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class APIKeyGenerator {
    public String generateApiKey(){
        return "AXLOGS_"+ UUID.randomUUID().toString().replace("-","");
    }
}
