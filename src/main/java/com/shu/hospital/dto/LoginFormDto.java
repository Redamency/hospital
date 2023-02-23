package com.shu.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Young
 * @date: 2023/2/20 22:48
 * @email: 1683209437@qq.com
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginFormDto {
    private String phone;
    private String password;
    private String code;
}