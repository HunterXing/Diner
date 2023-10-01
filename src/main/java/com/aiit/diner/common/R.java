package com.aiit.diner.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通用返回结果，服务端响应的数据最终都会封装成此对象
 * @param <T>
 */
@Data
@ApiModel(value = "通用返回对象", description = "通用返回对象")
public class R<T> {
    @ApiModelProperty(value = "返回码", name = "code", required = true)
    private Integer code;
    @ApiModelProperty(value = "信息", name = "msg", required = true)
    private String msg;
    @ApiModelProperty(value = "数据", name = "data")
    private T data;

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 500;
        return r;
    }

    public static <T> R<T> error(String msg, Integer code) {
        R r = new R();
        r.msg = msg;
        r.code = code;
        return r;
    }
}
