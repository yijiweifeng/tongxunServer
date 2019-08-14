package com.lt.domain.bean;

/**
 * @author sj
 * @date 2019/8/14 12:45
 */
public class JsonResult<T> {
    public static final String OK = "200";
    public static final String ERR = "-1";
    public static final JsonResult SUCESS = new JsonResult("200", "操作成功！");
    private String result;

    private String desc;

    private T data;

    public static JsonResult getFailResult(String message) {
        return new JsonResult("-1", message);
    }

    public static JsonResult getSuccessResult(String message) {
        return new JsonResult("200", message);
    }

    public static JsonResult getSuccessResult(Object data, String message) {
        return new JsonResult("200", message, data);
    }

    public JsonResult() {
        this.result = "-1";
        this.desc = "";
    }

    public JsonResult(String code, String message) {
        this.result = "-1";
        this.desc = "";
        this.result = code;
        this.desc = message;
    }

    public JsonResult(String code, String message, T result) {
        this.result = "-1";
        this.desc = "";
        this.result = code;
        this.desc = message;
        this.data = result;
    }

    public JsonResult(T result) {
        this("200", "操作成功！", result);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            JsonResult ret = (JsonResult)obj;
            return this.result == ret.getResult();
        }
    }

    public String getDesc() {
        return this.desc;
    }

    public JsonResult setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public String getResult() {
        return this.result;
    }

    public JsonResult setResult(String result) {
        this.result = result;
        return this;
    }

    public Object getData() {
        return this.data;
    }

    public JsonResult setData(T data) {
        this.data = data;
        return this;
    }
}
