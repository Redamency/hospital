package com.shu.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Young
 * @date: 2023/2/20 23:10
 * @email: 1683209437@qq.com
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userId;
    private String username;
    private String phone;
    private String code;
}