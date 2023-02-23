package com.shu.hospital.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName order
 */
@TableName(value ="order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    /**
     * 
     */
    @MppMultiId
    @TableField(value = "odr_no")
    private Integer odrNo;

    /**
     * 
     */
    @MppMultiId
    @TableField(value = "sub_no")
    private Integer subNo;

    /**
     * 
     */
    private Integer patientId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}