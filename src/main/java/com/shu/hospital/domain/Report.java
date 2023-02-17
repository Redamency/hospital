package com.shu.hospital.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 体检报告表
 * @TableName report
 */
@TableName(value ="report")
@Data
public class Report implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer reportSubno;

    /**
     * 
     */
    private Integer reportNo;

    /**
     * 
     */
    private Integer sbjtNo;

    /**
     * 
     */
    private String value;

    /**
     * 
     */
    private String note;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}