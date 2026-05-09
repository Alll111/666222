package com.controller;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.TokenEntity;
import com.entity.UserEntity;
import com.entity.YonghuEntity;
import com.service.TokenService;
import com.service.UserService;
import com.service.YonghuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class ApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private YonghuService yonghuService;

    @IgnoreAuth
    @PostMapping("/login")
    public Map<String, Object> login(String username, String password, String role, String tableName) {
        Map<String, Object> res = new HashMap<String, Object>();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            res.put("code", 500);
            res.put("message", "账号或密码不能为空");
            return res;
        }
        String token = null;
        if ("users".equals(tableName) || "管理员".equals(role)) {
            UserEntity user = userService.selectOne(new EntityWrapper<UserEntity>().eq("username", username));
            if (user != null && StringUtils.equals(user.getPassword(), password)) {
                token = tokenService.generateToken(user.getId(), user.getUsername(), "users", user.getRole());
            }
        } else if ("yonghu".equals(tableName) || "用户".equals(role)) {
            YonghuEntity yonghu = yonghuService.selectOne(new EntityWrapper<YonghuEntity>().eq("zhanghao", username));
            if (yonghu != null && StringUtils.equals(yonghu.getMima(), password)) {
                token = tokenService.generateToken(yonghu.getId(), yonghu.getZhanghao(), "yonghu", "用户");
            }
        } else {
            UserEntity user = userService.selectOne(new EntityWrapper<UserEntity>().eq("username", username));
            if (user != null && StringUtils.equals(user.getPassword(), password)) {
                token = tokenService.generateToken(user.getId(), user.getUsername(), "users", user.getRole());
            } else {
                YonghuEntity yonghu = yonghuService.selectOne(new EntityWrapper<YonghuEntity>().eq("zhanghao", username));
                if (yonghu != null && StringUtils.equals(yonghu.getMima(), password)) {
                    token = tokenService.generateToken(yonghu.getId(), yonghu.getZhanghao(), "yonghu", "用户");
                }
            }
        }
        if (StringUtils.isBlank(token)) {
            res.put("code", 500);
            res.put("message", "账号或密码不正确");
            return res;
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("token", token);
        res.put("code", 200);
        res.put("data", data);
        res.put("message", "");
        return res;
    }

    @GetMapping("/menu/list")
    public Map<String, Object> menuList(HttpServletRequest request) {
        Map<String, Object> res = new HashMap<String, Object>();
        List<Map<String, Object>> menu = new ArrayList<Map<String, Object>>();
        String token = request.getHeader("Token");
        if (StringUtils.isBlank(token)) {
            String authorization = request.getHeader("Authorization");
            if (StringUtils.isNotBlank(authorization) && authorization.startsWith("Bearer ")) {
                token = authorization.substring(7);
            }
        }
        TokenEntity tokenEntity = StringUtils.isBlank(token) ? null : tokenService.getTokenEntity(token);
        if (tokenEntity != null && "管理员".equals(tokenEntity.getRole())) {
            menu.add(menuItem(1, "用户管理", "/index/yonghu", "views/modules/yonghu/list"));
            menu.add(menuItem(2, "线下活动管理", "/index/xianxiahuodong", "views/modules/xianxiahuodong/list"));
            menu.add(menuItem(3, "交友信息管理", "/index/jiaoyouxinxi", "views/modules/jiaoyouxinxi/list"));
            menu.add(menuItem(4, "活动报名管理", "/index/huodongbaoming", "views/modules/huodongbaoming/list"));
            menu.add(menuItem(5, "交流论坛", "/index/forum", "views/modules/forum/list"));
            menu.add(menuItem(6, "公告信息", "/index/news", "views/modules/news/list"));
            menu.add(menuItem(7, "轮播图管理", "/index/config", "views/modules/config/list"));
        } else if (tokenEntity != null) {
            menu.add(menuItem(1, "交友信息管理", "/index/jiaoyouxinxi", "views/modules/jiaoyouxinxi/list"));
            menu.add(menuItem(2, "活动报名管理", "/index/huodongbaoming", "views/modules/huodongbaoming/list"));
        }
        res.put("code", 200);
        res.put("data", menu);
        res.put("message", "成功");
        return res;
    }

    @IgnoreAuth
    @GetMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        Map<String, Object> res = new HashMap<String, Object>();
        String token = request.getHeader("Token");
        if (StringUtils.isBlank(token)) {
            String authorization = request.getHeader("Authorization");
            if (StringUtils.isNotBlank(authorization) && authorization.startsWith("Bearer ")) {
                token = authorization.substring(7);
            }
        }
        if (StringUtils.isNotBlank(token)) {
            TokenEntity tokenEntity = tokenService.getTokenEntity(token);
            if (tokenEntity != null) {
                tokenEntity.setExpiratedtime(new Date());
                tokenService.updateById(tokenEntity);
            }
        }
        request.getSession().invalidate();
        res.put("code", 200);
        res.put("message", "退出成功");
        return res;
    }

    private Map<String, Object> menuItem(int id, String name, String path, String component) {
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("id", id);
        item.put("name", name);
        item.put("path", path);
        item.put("component", component);
        return item;
    }
}
