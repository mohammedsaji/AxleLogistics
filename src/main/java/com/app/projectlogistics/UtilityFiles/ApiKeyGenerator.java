package com.app.projectlogistics.UtilityFiles;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ApiKeyGenerator {

    public String generateApiKey(){
        return "PSL"+ UUID.randomUUID() + "KY";
    }
}
