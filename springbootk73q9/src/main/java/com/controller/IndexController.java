package com.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.annotation.IgnoreAuth;

@RestController
public class IndexController {

    @IgnoreAuth
    @GetMapping("/")
    public Map<String, Object> index() {
        Map<String, Object> res = new LinkedHashMap<String, Object>();
        res.put("code", 0);
        res.put("msg", "API 服务运行中");
        res.put("frontend", "请通过 Vue 前端 admin 项目访问系统");
        return res;
    }
}
