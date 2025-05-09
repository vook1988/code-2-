package com.example.image_label.utils;

import java.io.Serializable;

// 泛型类，用于封装接口返回结果
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    // 状态码
    private int code;
    // 消息
    private String msg;
    // 数据
    private T data;

    // 无参构造方法
    public Result() {
    }

    // 带状态码和消息的构造方法
    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // 带状态码、消息和数据的构造方法
    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功返回结果的静态方法，无数据
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功");
    }

    // 成功返回结果的静态方法，带数据
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 成功返回结果的静态方法，带消息和数据
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(200, msg, data);
    }

    // 失败返回结果的静态方法，带消息
    public static <T> Result<T> error(String msg) {
        return new Result<>(500, msg);
    }

    // 失败返回结果的静态方法，带状态码和消息
    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg);
    }

    // Getter 和 Setter 方法
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}