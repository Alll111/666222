package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.JiaoyouxinxiEntity;
import com.entity.view.JiaoyouxinxiView;

import com.service.JiaoyouxinxiService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.service.StoreupService;
import com.service.HaoyouService;
import com.entity.HaoyouEntity;
import com.service.YonghuService;
import com.entity.YonghuEntity;

/**
 * 交友信息
 * 后端接口
 * @author 
 * @email 
 * @date 2022-04-03 21:46:28
 */
@RestController
@RequestMapping("/jiaoyouxinxi")
public class JiaoyouxinxiController {
    @Autowired
    private JiaoyouxinxiService jiaoyouxinxiService;

    @Autowired
    private StoreupService storeupService;

    @Autowired
    private HaoyouService haoyouService;

    @Autowired
    private YonghuService yonghuService;

    


    /**
     * 后端列表
     */
    @RequestMapping({"/page", "/getpage"})
    public R page(@RequestParam Map<String, Object> params,JiaoyouxinxiEntity jiaoyouxinxi,
		HttpServletRequest request){
		Object tableNameObj = request.getSession().getAttribute("tableName");
		String tableName = tableNameObj == null ? "" : String.valueOf(tableNameObj);
		if("yonghu".equals(tableName)) {
			Long userId = (Long)request.getSession().getAttribute("userId");
			if (userId == null) {
				return R.error("未登录");
			}
			List<HaoyouEntity> haoyouList = haoyouService.selectList(new EntityWrapper<HaoyouEntity>().eq("userid", userId));
			List<Long> userIds = new ArrayList<Long>();
			userIds.add(userId);
			for(HaoyouEntity h : haoyouList) {
				if(h.getFriendUserid() != null) {
					userIds.add(h.getFriendUserid());
				}
			}
			if(userIds.size() > 0) {
				List<YonghuEntity> yonghuList = yonghuService.selectList(new EntityWrapper<YonghuEntity>().in("id", userIds));
				List<String> zhanghaoList = new ArrayList<String>();
				for(YonghuEntity y : yonghuList) {
					if(y.getZhanghao() != null) {
						zhanghaoList.add(y.getZhanghao());
					}
				}
				if(zhanghaoList.size() > 0) {
					params.put("zhanghaoList", zhanghaoList);
				}
			}
		}
        Wrapper<JiaoyouxinxiEntity> wrapper = new EntityWrapper<JiaoyouxinxiEntity>();
		if(params.get("zhanghaoList") != null) {
			@SuppressWarnings("unchecked")
			List<String> zhanghaoList = (List<String>)params.get("zhanghaoList");
			if(zhanghaoList.size() > 0) {
				wrapper.in("zhanghao", zhanghaoList);
			}
		}
		wrapper = MPUtil.likeOrEq(wrapper, jiaoyouxinxi);
		wrapper = MPUtil.between(wrapper, params);
		wrapper = MPUtil.sort(wrapper, params);
		PageUtils page = jiaoyouxinxiService.queryPage(params, wrapper);

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,JiaoyouxinxiEntity jiaoyouxinxi, 
		HttpServletRequest request){
        Wrapper<JiaoyouxinxiEntity> wrapper = new EntityWrapper<JiaoyouxinxiEntity>();
		wrapper = MPUtil.likeOrEq(wrapper, jiaoyouxinxi);
		wrapper = MPUtil.between(wrapper, params);
		wrapper = MPUtil.sort(wrapper, params);
		PageUtils page = jiaoyouxinxiService.queryPage(params, wrapper);
        return R.ok().put("data", page);
    }

	/**
     * 当前用户发布列表
     */
    @RequestMapping("/myPage")
    public R myPage(@RequestParam Map<String, Object> params, HttpServletRequest request){
        if (!isYonghuRequest(request)) {
            return R.error("未登录");
        }
        String username = getCurrentUsername(request);
        if (StringUtils.isBlank(username)) {
            return R.error("未找到当前用户信息");
        }
        Wrapper<JiaoyouxinxiEntity> wrapper = new EntityWrapper<JiaoyouxinxiEntity>()
                .eq("zhanghao", username)
                .orderBy("addtime", false);
        if (params.get("sfsh") != null && StringUtils.isNotBlank(params.get("sfsh").toString())) {
            wrapper.eq("sfsh", params.get("sfsh").toString().trim());
        }
        wrapper = MPUtil.sort(wrapper, params);
        PageUtils page = jiaoyouxinxiService.queryPage(params, wrapper);
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( JiaoyouxinxiEntity jiaoyouxinxi){
       	EntityWrapper<JiaoyouxinxiEntity> ew = new EntityWrapper<JiaoyouxinxiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( jiaoyouxinxi, "jiaoyouxinxi")); 
        return R.ok().put("data", jiaoyouxinxiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(JiaoyouxinxiEntity jiaoyouxinxi){
        EntityWrapper< JiaoyouxinxiEntity> ew = new EntityWrapper< JiaoyouxinxiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( jiaoyouxinxi, "jiaoyouxinxi")); 
		JiaoyouxinxiView jiaoyouxinxiView =  jiaoyouxinxiService.selectView(ew);
		return R.ok("查询交友信息成功").put("data", jiaoyouxinxiView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        JiaoyouxinxiEntity jiaoyouxinxi = jiaoyouxinxiService.selectById(id);
		jiaoyouxinxi.setClicknum(jiaoyouxinxi.getClicknum()+1);
		jiaoyouxinxi.setClicktime(new Date());
		jiaoyouxinxiService.updateById(jiaoyouxinxi);
        return R.ok().put("data", jiaoyouxinxi);
    }

    /**
     * 当前用户发布详情
     */
    @RequestMapping("/myInfo/{id}")
    public R myInfo(@PathVariable("id") Long id, HttpServletRequest request){
        if (!isYonghuRequest(request)) {
            return R.error("未登录");
        }
        JiaoyouxinxiEntity entity = jiaoyouxinxiService.selectById(id);
        if (entity == null) {
            return R.error("记录不存在");
        }
        String username = (String) request.getSession().getAttribute("username");
        if (!username.equals(entity.getZhanghao())) {
            return R.error("无权查看该记录");
        }
        return R.ok().put("data", entity);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        JiaoyouxinxiEntity jiaoyouxinxi = jiaoyouxinxiService.selectById(id);
		jiaoyouxinxi.setClicknum(jiaoyouxinxi.getClicknum()+1);
		jiaoyouxinxi.setClicktime(new Date());
		jiaoyouxinxiService.updateById(jiaoyouxinxi);
        return R.ok().put("data", jiaoyouxinxi);
    }
    


    /**
     * 赞或踩
     */
    @RequestMapping("/thumbsup/{id}")
    public R vote(@PathVariable("id") String id,String type){
        JiaoyouxinxiEntity jiaoyouxinxi = jiaoyouxinxiService.selectById(id);
        if(type.equals("1")) {
        	jiaoyouxinxi.setThumbsupnum(jiaoyouxinxi.getThumbsupnum()+1);
        } else {
        	jiaoyouxinxi.setCrazilynum(jiaoyouxinxi.getCrazilynum()+1);
        }
        jiaoyouxinxiService.updateById(jiaoyouxinxi);
        return R.ok("投票成功");
    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody JiaoyouxinxiEntity jiaoyouxinxi, HttpServletRequest request){
    	jiaoyouxinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(jiaoyouxinxi);
        jiaoyouxinxiService.insert(jiaoyouxinxi);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody JiaoyouxinxiEntity jiaoyouxinxi, HttpServletRequest request){
    	jiaoyouxinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(jiaoyouxinxi);
        if (isYonghuRequest(request)) {
            YonghuEntity currentUser = getCurrentYonghu(request);
            if (currentUser == null) {
                return R.error("未登录");
            }
            jiaoyouxinxi.setZhanghao(currentUser.getZhanghao());
            jiaoyouxinxi.setXingming(currentUser.getXingming());
            jiaoyouxinxi.setXingbie(currentUser.getXingbie());
            jiaoyouxinxi.setNianling(currentUser.getNianling());
            jiaoyouxinxi.setSfsh("待审核");
            jiaoyouxinxi.setShhf("");
            if (jiaoyouxinxi.getThumbsupnum() == null) {
                jiaoyouxinxi.setThumbsupnum(0);
            }
            if (jiaoyouxinxi.getCrazilynum() == null) {
                jiaoyouxinxi.setCrazilynum(0);
            }
            if (jiaoyouxinxi.getClicknum() == null) {
                jiaoyouxinxi.setClicknum(0);
            }
        }
        jiaoyouxinxiService.insert(jiaoyouxinxi);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody JiaoyouxinxiEntity jiaoyouxinxi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(jiaoyouxinxi);
        jiaoyouxinxiService.updateById(jiaoyouxinxi);//全部更新
        return R.ok();
    }

    /**
     * 当前用户修改自己的交友信息
     */
    @RequestMapping("/myUpdate")
    public R myUpdate(@RequestBody JiaoyouxinxiEntity jiaoyouxinxi, HttpServletRequest request){
        if (!isYonghuRequest(request)) {
            return R.error("未登录");
        }
        if (jiaoyouxinxi.getId() == null) {
            return R.error("缺少记录ID");
        }
        JiaoyouxinxiEntity oldEntity = jiaoyouxinxiService.selectById(jiaoyouxinxi.getId());
        if (oldEntity == null) {
            return R.error("记录不存在");
        }
        String username = (String) request.getSession().getAttribute("username");
        if (!username.equals(oldEntity.getZhanghao())) {
            return R.error("无权修改该记录");
        }
        YonghuEntity currentUser = getCurrentYonghu(request);
        if (currentUser == null) {
            return R.error("未登录");
        }
        oldEntity.setZhanghao(currentUser.getZhanghao());
        oldEntity.setXingming(currentUser.getXingming());
        oldEntity.setXingbie(currentUser.getXingbie());
        oldEntity.setNianling(currentUser.getNianling());
        oldEntity.setXingquaihao(jiaoyouxinxi.getXingquaihao());
        oldEntity.setJiaoyoumude(jiaoyouxinxi.getJiaoyoumude());
        oldEntity.setGerenjianjie(jiaoyouxinxi.getGerenjianjie());
        oldEntity.setJiaoyoutupian(jiaoyouxinxi.getJiaoyoutupian());
        oldEntity.setSfsh("待审核");
        oldEntity.setShhf("");
        jiaoyouxinxiService.updateById(oldEntity);
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        jiaoyouxinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 当前用户删除自己的交友信息
     */
    @RequestMapping("/myDelete")
    public R myDelete(@RequestBody Long[] ids, HttpServletRequest request){
        if (!isYonghuRequest(request)) {
            return R.error("未登录");
        }
        if (ids == null || ids.length == 0) {
            return R.ok();
        }
        String username = (String) request.getSession().getAttribute("username");
        for (Long id : ids) {
            JiaoyouxinxiEntity entity = jiaoyouxinxiService.selectById(id);
            if (entity != null && username.equals(entity.getZhanghao())) {
                jiaoyouxinxiService.deleteById(id);
            }
        }
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		
		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date()); 
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}
		
		Wrapper<JiaoyouxinxiEntity> wrapper = new EntityWrapper<JiaoyouxinxiEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yonghu")) {
			wrapper.eq("zhanghao", (String)request.getSession().getAttribute("username"));
		}

		int count = jiaoyouxinxiService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	
	/**
     * 前端智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,JiaoyouxinxiEntity jiaoyouxinxi, HttpServletRequest request,String pre){
        Wrapper<JiaoyouxinxiEntity> wrapper = new EntityWrapper<JiaoyouxinxiEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
		Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String newKey = entry.getKey();
			if (pre.endsWith(".")) {
				newMap.put(pre + newKey, entry.getValue());
			} else if (StringUtils.isEmpty(pre)) {
				newMap.put(newKey, entry.getValue());
			} else {
				newMap.put(pre + "." + newKey, entry.getValue());
			}
		}
		params.put("sort", "clicknum");
        params.put("order", "desc");
		wrapper = MPUtil.likeOrEq(wrapper, jiaoyouxinxi);
		wrapper = MPUtil.between(wrapper, params);
		wrapper = MPUtil.sort(wrapper, params);
		PageUtils page = jiaoyouxinxiService.queryPage(params, wrapper);
        return R.ok().put("data", page);
    }

    private boolean isYonghuRequest(HttpServletRequest request) {
        Object tableName = request.getSession().getAttribute("tableName");
        return tableName != null && "yonghu".equals(String.valueOf(tableName));
    }

    private YonghuEntity getCurrentYonghu(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return null;
        }
        return yonghuService.selectById(userId);
    }

    private String getCurrentUsername(HttpServletRequest request) {
        Object username = request.getSession().getAttribute("username");
        if (username != null && StringUtils.isNotBlank(String.valueOf(username))) {
            return String.valueOf(username);
        }
        YonghuEntity currentUser = getCurrentYonghu(request);
        return currentUser == null ? null : currentUser.getZhanghao();
    }







}
