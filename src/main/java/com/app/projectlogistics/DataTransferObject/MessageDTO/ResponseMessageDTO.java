package com.app.projectlogistics.DataTransferObject.MessageDTO;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseMessageDTO {

    private Integer statusCode;

    private String Message;

    private Map<String,Object> valueMap = new HashMap<>();

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, Object> getValueMap() {
        return valueMap;
    }

    public void setValue(String name, Object value) {
        valueMap.put(name ,value);
    }

    public void clearValueMap(){
        valueMap.clear();
    }
}
