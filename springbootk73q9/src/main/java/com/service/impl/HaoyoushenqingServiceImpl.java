package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;

import com.dao.HaoyoushenqingDao;
import com.entity.HaoyoushenqingEntity;
import com.service.HaoyoushenqingService;
import com.entity.view.HaoyoushenqingView;

@Service("haoyoushenqingService")
public class HaoyoushenqingServiceImpl extends ServiceImpl<HaoyoushenqingDao, HaoyoushenqingEntity> implements HaoyoushenqingService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<HaoyoushenqingEntity> page = this.selectPage(
                new Query<HaoyoushenqingEntity>(params).getPage(),
                new EntityWrapper<HaoyoushenqingEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<HaoyoushenqingEntity> wrapper) {
        Page<HaoyoushenqingView> page = new Query<HaoyoushenqingView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<HaoyoushenqingView> selectListView(Wrapper<HaoyoushenqingEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public HaoyoushenqingView selectView(Wrapper<HaoyoushenqingEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}

