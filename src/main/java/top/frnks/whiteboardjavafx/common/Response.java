package top.frnks.whiteboardjavafx.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Response implements Serializable {
    private ResponseType responseType;
    private final Map<String, Object> dataMap = new HashMap<>();

    public Response(ResponseType responseType) {
        this.responseType = responseType;
    }
    public Response() {}
    public void setData(String key, Object value) {
        this.dataMap.put(key, value);
    }
    public Object getData(String key) {
        return this.dataMap.get(key);
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }
}
