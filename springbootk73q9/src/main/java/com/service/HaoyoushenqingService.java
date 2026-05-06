package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.HaoyoushenqingEntity;
import java.util.List;
import java.util.Map;
import com.entity.view.HaoyoushenqingView;
import org.apache.ibatis.annotations.Param;

public interface HaoyoushenqingService extends IService<HaoyoushenqingEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<HaoyoushenqingView> selectListView(Wrapper<HaoyoushenqingEntity> wrapper);

    HaoyoushenqingView selectView(@Param("ew") Wrapper<HaoyoushenqingEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<HaoyoushenqingEntity> wrapper);
}

