package com.example.producttrading.utils;

import lombok.Data;

/**
 * @author guoxin
 * @date 2026年04月02日 16:13
 */
@Data
public class Result {
    private Integer code; //状态码
    private String message; //提示信息
    private Object data;    // 数据
    private Long timestamp; //时间戳

    //构造方法私有化
    private Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    public static Result success() {
        return success(null);
    }

    public static Result error(String message) {
        Result result = new Result();
        result.setCode(500);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
    public static Result error(Integer code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

}
