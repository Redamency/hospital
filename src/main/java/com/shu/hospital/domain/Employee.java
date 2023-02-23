package com.shu.hospital.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName employee
 */
@TableName(value ="employee")
@Data
public class Employee implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String pname;

    /**
     *
     */
    private String password;

    /**
     * 
     */
    private Date birthday;

    /**
     * 
     */
    private Integer gender;

    /**
     * 
     */
    private String telephone;

    /**
     * 
     */
    private String department;

    /**
     * 
     */
    private String position;

    /**
     * 
     */
    private Double salary;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}