package net.pixelcop.monstrous.http;

import net.sf.json.JSONObject;

public class JsonUtils {
    
    public static String toJsonString(Object object) {
        return JSONObject.fromObject(object).toString();
    }
    
    @SuppressWarnings("unchecked")
    public static Object fromJsonString(String json, Class clazz) {
        JSONObject jsonObject = JSONObject.fromObject(json);
        return JSONObject.toBean(jsonObject, clazz);
    }

}
