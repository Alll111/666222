package com.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.HaoyouDao;
import com.entity.HaoyouEntity;
import com.service.HaoyouService;

@Service("haoyouService")
public class HaoyouServiceImpl extends ServiceImpl<HaoyouDao, HaoyouEntity> implements HaoyouService {
}

