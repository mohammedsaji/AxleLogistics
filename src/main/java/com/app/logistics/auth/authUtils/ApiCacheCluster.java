package com.app.logistics.auth.authUtils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class ApiCacheCluster {
    private Map<String, Map<String,LocalDateTime>> cacheCluster = new HashMap<>();

    public void setAPIKey(String username, String apiKey){
        cacheCluster.put(username,Map.of(apiKey,LocalDateTime.now().plusMinutes(15)));
    }

    public Map<String,LocalDateTime> getAPIKey(String username){
        return cacheCluster.get(username);
    }

    public void removeAPIKey(String userName) {
        cacheCluster.remove(userName);
    }
}
