package top.frnks.whiteboardjavafx.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Request implements Serializable {
    private RequestType requestType;
    private final Map<String, Object> dataMap = new HashMap<>();

    public Request(RequestType requestType) {
        this.requestType = requestType;
    }
    public Request() {}
    public void setData(String key, Object value) {
        this.dataMap.put(key, value);
    }

    public Object getData(String key) {
        return this.dataMap.get(key);
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }
}
