package com.dao;

import com.entity.HaoyoushenqingEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Param;
import com.entity.view.HaoyoushenqingView;

public interface HaoyoushenqingDao extends BaseMapper<HaoyoushenqingEntity> {

    List<HaoyoushenqingView> selectListView(@Param("ew") Wrapper<HaoyoushenqingEntity> wrapper);

    List<HaoyoushenqingView> selectListView(Pagination page, @Param("ew") Wrapper<HaoyoushenqingEntity> wrapper);

    HaoyoushenqingView selectView(@Param("ew") Wrapper<HaoyoushenqingEntity> wrapper);
}

