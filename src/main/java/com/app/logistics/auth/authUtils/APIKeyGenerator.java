package com.app.logistics.auth.authUtils;

import java.util.UUID;

public class APIKeyGenerator {
    public String generateApiKey(){
        return "AXLOGS_"+ UUID.randomUUID().toString().replace("-","");
    }
}
