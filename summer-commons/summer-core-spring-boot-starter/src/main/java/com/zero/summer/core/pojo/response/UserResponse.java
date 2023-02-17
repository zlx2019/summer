package com.zero.summer.core.pojo.response;

import com.zero.summer.core.enums.Gender;

/**
 * @author Zero.
 * @date 2023/2/17 12:32 AM
 */
public record UserResponse(Long id, String username, String nickname, Gender gender) {

}
