package com.app.projectlogistics.UtilityFiles;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ApiKeyCache {

    private Map<String,Map<String,Long>> apiKeyCache = new HashMap<>();

    public Map<String,Long> getApiCache(String employeeUserName){
        return apiKeyCache.get(employeeUserName);
    }

    public void addKeysInApiCache(String employeeUserName, Map<String,Long>apiKey){
        apiKeyCache.put(employeeUserName,apiKey);
    }

    public void removeKeysInApiCache(String employeeUserName){
        apiKeyCache.remove(employeeUserName);
    }
}
