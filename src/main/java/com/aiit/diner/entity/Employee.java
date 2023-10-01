package com.aiit.diner.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体
 */
@ApiModel(value = "员工实体类", description = "员工实体类")
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "员工账户")
    private String username;

    @ApiModelProperty(value = "员工姓名")
    private String name;

    @ApiModelProperty(value = "员工密码")
    private String password;

    @ApiModelProperty(value = "员工手机")
    private String phone;

    @ApiModelProperty(value = "员工性别")
    private String sex;

    @ApiModelProperty(value = "身份证号码")
    private String idNumber;

    @ApiModelProperty(value = "员工状态 0：禁 用 1：正常")
    private Integer status;


    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
