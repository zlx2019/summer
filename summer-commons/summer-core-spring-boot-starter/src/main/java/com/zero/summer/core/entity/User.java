package com.zero.summer.core.entity;

import com.zero.summer.core.entity.abstracts.SuperModel;
import com.zero.summer.core.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author Zero.
 * @date 2023/2/16 10:59 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class User extends SuperModel<User> implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 用户名
     * 唯一索引
     */
    private String username;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 性别
     * 0-未知 1-男 2-女
     */
    private Gender gender;
    /**
     * 用户是否锁定
     */
    private Boolean locked;
    /**
     * 用户是否可用
     */
    private Boolean activated;
    /**
     * 最后登录IP
     */
    private String lastLoginIp;
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    public User() {
        this.setActivated(Boolean.TRUE);
        this.setLocked(Boolean.FALSE);
    }

    /**
     * 获取用户的权限列表
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * 当前用户是否过期(未使用该业务)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 当前用户是否锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return !getLocked();
    }

    /**
     * 凭证是否过期(未使用该业务)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 当前用户是否可用
     */
    @Override
    public boolean isEnabled() {
        return getActivated();
    }
}
