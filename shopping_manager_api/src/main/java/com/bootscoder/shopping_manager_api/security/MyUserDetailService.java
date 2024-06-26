package com.bootscoder.shopping_manager_api.security;

import com.bootscoder.shopping_common.pojo.Admin;
import com.bootscoder.shopping_common.pojo.Permission;
import com.bootscoder.shopping_common.service.AdminService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证授权逻辑
 *
 * @author bootsCoder
 * @date created on 2024/4/18
 */
@Service
public class MyUserDetailService implements UserDetailsService {
    @DubboReference
    private AdminService adminService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.认证
        Admin admin = adminService.findByAdminName(username);
        if(admin == null){
            throw new UsernameNotFoundException("用户不存在");
        }

        // 2.授权
        List<Permission> permissions = adminService.findAllPermission(username);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (permissions.get(0) != null){
            for (Permission permission : permissions) {
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getUrl()));
            }
        }


        // 3.封装为UserDetails对象
        UserDetails userDetails = User.withUsername(admin.getUsername())
                .password(admin.getPassword())
                .authorities(grantedAuthorities)
                .build();


        // 4.返回封装好的UserDetails对象
        return userDetails;
    }
}
