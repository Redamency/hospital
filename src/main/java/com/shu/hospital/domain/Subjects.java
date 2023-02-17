package com.shu.hospital.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 体检项目表
 * @TableName subjects
 */
@TableName(value ="subjects")
@Data
public class Subjects implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer sbjtNo;

    /**
     * 
     */
    private String snjtName;

    /**
     * 
     */
    private Integer deptNo;

    /**
     * 
     */
    private String unit;

    /**
     * 
     */
    private Double cost;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}