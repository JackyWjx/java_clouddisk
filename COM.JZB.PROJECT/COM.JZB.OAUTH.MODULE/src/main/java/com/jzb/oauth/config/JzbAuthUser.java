package com.jzb.oauth.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 认证用户
 * @author Chad
 * @date 2019年08月01日
 */
public class JzbAuthUser extends User {
    /**
     * 构造方法
     * @param uid
     * @param pwd
     * @param authorities
     */
    public JzbAuthUser(String uid, String pwd, Collection<? extends GrantedAuthority> authorities) {
        super(uid, pwd, authorities);
    } // End JzbAuthUser

    /**
     * 构造方法
     * @param uid
     * @param pwd
     * @param enabled
     * @param accountNonExpired
     * @param credentialsNonExpired
     * @param accountNonLocked
     * @param authorities
     */
    public JzbAuthUser(String uid, String pwd, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(uid, pwd, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    } // End JzbAuthUser
} // End class JzbAuthUser
