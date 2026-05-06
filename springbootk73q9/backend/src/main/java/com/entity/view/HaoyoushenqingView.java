package com.entity.view;

import com.entity.HaoyoushenqingEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;

@TableName("haoyoushenqing")
public class HaoyoushenqingView extends HaoyoushenqingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public HaoyoushenqingView() {
    }

    public HaoyoushenqingView(HaoyoushenqingEntity entity) {
        try {
            BeanUtils.copyProperties(this, entity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

