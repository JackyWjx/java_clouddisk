package com.jzb.oauth.service;

import com.alibaba.fastjson.JSON;
import com.jzb.oauth.config.JzbAuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户认证处理
 *
 * @author Chad
 * @date 2019年08月01日
 */
@Slf4j
@Service
public class JzbUserDetailsService implements UserDetailsService {
    /**
     * 密码处理
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JzbUserService userService;

    @Autowired
    private JzbPermissionService permissionService;

    /**
     * @param phone
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        JzbAuthUser result;
        try {
            // 查询用户信息
            Map<String, Object> jzbUser = userService.getUserByPhone(phone);
            if (jzbUser != null && jzbUser.size() > 0) {
                // 用户权限暂时不考虑，登录后在权限模块再去获取
//            List<Map<String, Object>> permissionList = permissionService.findByUserId(jzbUser.get("uid").toString());
//            if (!CollectionUtils.isEmpty(permissionList)) {
//                for (int i = 0, l = permissionList.size(); i < l; i++) {
//                    Map<String, Object> record = permissionList.get(i);
//                }
//            }

                System.out.println("=================================>> " + JSON.toJSONString(jzbUser));
                List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
                authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
                result = new JzbAuthUser(jzbUser.get("uid").toString(), jzbUser.get("passwd").toString(), authorityList);
            } else {
                result = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = null;
        }

        return result;
    } // End loadUserByUsername
} // End class JzbUserDetailsService
