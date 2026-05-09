package com.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import com.entity.HaoyoushenqingEntity;
import com.entity.HaoyouEntity;
import com.entity.JiaoyouxinxiEntity;
import com.entity.SiliaoHuihuaEntity;
import com.entity.SiliaoXiaoxiEntity;
import com.entity.YonghuEntity;
import com.service.HaoyoushenqingService;
import com.service.HaoyouService;
import com.service.JiaoyouxinxiService;
import com.service.SiliaoHuihuaService;
import com.service.SiliaoXiaoxiService;
import com.service.YonghuService;
import com.utils.PageUtils;
import com.utils.R;
import com.baomidou.mybatisplus.plugins.Page;

@RestController
@RequestMapping("/haoyoushenqing")
public class HaoyoushenqingController {

    @Autowired
    private HaoyoushenqingService haoyoushenqingService;

    @Autowired
    private HaoyouService haoyouService;

    @Autowired
    private YonghuService yonghuService;

    @Autowired
    private SiliaoHuihuaService siliaoHuihuaService;

    @Autowired
    private SiliaoXiaoxiService siliaoXiaoxiService;

    @Autowired
    private JiaoyouxinxiService jiaoyouxinxiService;

    @RequestMapping("/send")
    public R send(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Long fromUserId = (Long) request.getSession().getAttribute("userId");
        String fromZhanghao = (String) request.getSession().getAttribute("username");
        if (fromUserId == null) {
            return R.error("未登录");
        }
        String toZhanghao = body.get("toZhanghao") == null ? null : body.get("toZhanghao").toString();
        if (toZhanghao == null) {
            return R.error("缺少对方账号");
        }
        if (fromZhanghao != null && fromZhanghao.equals(toZhanghao)) {
            return R.error("不能添加自己为好友");
        }
        YonghuEntity toUser = yonghuService.selectOne(new EntityWrapper<YonghuEntity>().eq("zhanghao", toZhanghao));
        if (toUser == null) {
            return R.error("对方账号不存在");
        }
        Long toUserId = toUser.getId();
        HaoyouEntity existFriend = haoyouService.selectOne(
                new EntityWrapper<HaoyouEntity>().eq("userid", fromUserId).eq("friend_userid", toUserId));
        if (existFriend != null) {
            return R.error("已是好友");
        }
        HaoyoushenqingEntity exist = haoyoushenqingService.selectOne(
                new EntityWrapper<HaoyoushenqingEntity>()
                        .eq("from_userid", fromUserId)
                        .eq("to_userid", toUserId)
                        .eq("status", "待处理"));
        if (exist != null) {
            return R.error("已发送申请，请等待对方处理");
        }
        YonghuEntity fromUser = yonghuService.selectById(fromUserId);
        HaoyoushenqingEntity entity = new HaoyoushenqingEntity();
        entity.setId(new Date().getTime() + (long) Math.floor(Math.random() * 1000));
        entity.setFromUserid(fromUserId);
        entity.setFromZhanghao(fromZhanghao);
        entity.setFromXingming(fromUser == null ? null : fromUser.getXingming());
        entity.setToUserid(toUserId);
        entity.setToZhanghao(toZhanghao);
        entity.setToXingming(toUser.getXingming());
        entity.setStatus("待处理");
        if (body.get("refJiaoyouxinxiId") != null) {
            entity.setRefJiaoyouxinxiId(Long.valueOf(body.get("refJiaoyouxinxiId").toString()));
        }
        haoyoushenqingService.insert(entity);
        return R.ok();
    }

    @RequestMapping("/inboxCount")
    public R inboxCount(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return R.ok().put("count", 0);
        }
        int count = haoyoushenqingService.selectCount(
                new EntityWrapper<HaoyoushenqingEntity>()
                        .eq("to_userid", userId)
                        .eq("status", "待处理"));
        return R.ok().put("count", count);
    }

    @RequestMapping("/inboxList")
    public R inboxList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return R.error("未登录");
        }
        Wrapper<HaoyoushenqingEntity> wrapper = new EntityWrapper<HaoyoushenqingEntity>()
                .eq("to_userid", userId)
                .orderBy("addtime", false);
        if (params.get("status") != null && params.get("status").toString().trim().length() > 0) {
            wrapper.eq("status", params.get("status").toString().trim());
        }
        PageUtils page = haoyoushenqingService.queryPage(params, wrapper);
        return R.ok().put("data", page);
    }

    @RequestMapping("/friendList")
    public R friendList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return R.error("未登录");
        }
        int pageNum = params.get("page") == null ? 1 : Integer.parseInt(params.get("page").toString());
        int limit = params.get("limit") == null ? 10 : Integer.parseInt(params.get("limit").toString());
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (limit <= 0) {
            limit = 10;
        }
        Page<HaoyouEntity> page = new Page<HaoyouEntity>(pageNum, limit);
        List<HaoyouEntity> records = haoyouService.selectPage(
                page,
                new EntityWrapper<HaoyouEntity>().eq("userid", userId).orderBy("addtime", false)
        ).getRecords();

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Date now = new Date();
        for (HaoyouEntity relation : records) {
            if (relation == null || relation.getFriendUserid() == null) {
                continue;
            }
            Long friendUserId = relation.getFriendUserid();
            YonghuEntity friendUser = yonghuService.selectById(friendUserId);
            if (friendUser == null) {
                continue;
            }

            int unreadCount = siliaoXiaoxiService.selectCount(
                    new EntityWrapper<SiliaoXiaoxiEntity>()
                            .eq("from_userid", friendUserId)
                            .eq("to_userid", userId)
                            .eq("is_read", 0)
            );

            Long user1Id = userId < friendUserId ? userId : friendUserId;
            Long user2Id = userId < friendUserId ? friendUserId : userId;
            SiliaoHuihuaEntity<?> session = siliaoHuihuaService.selectOne(
                    new EntityWrapper<SiliaoHuihuaEntity>()
                            .eq("user1_id", user1Id)
                            .eq("user2_id", user2Id)
            );

            JiaoyouxinxiEntity latestJiaoyou = jiaoyouxinxiService.selectOne(
                    new EntityWrapper<JiaoyouxinxiEntity>()
                            .eq("zhanghao", friendUser.getZhanghao())
                            .orderBy("addtime", false)
                            .last("limit 1")
            );

            String onlineStatus = "离线";
            if (session != null && session.getLastTime() != null) {
                long diff = now.getTime() - session.getLastTime().getTime();
                if (diff >= 0 && diff <= 10 * 60 * 1000L) {
                    onlineStatus = "在线";
                }
            }

            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", friendUser.getId());
            item.put("relationId", relation.getId());
            item.put("friendUserId", friendUserId);
            item.put("zhanghao", friendUser.getZhanghao());
            item.put("xingming", friendUser.getXingming());
            item.put("xingbie", friendUser.getXingbie());
            item.put("nianling", friendUser.getNianling());
            item.put("shouji", friendUser.getShouji());
            item.put("touxiang", friendUser.getTouxiang());
            item.put("onlineStatus", onlineStatus);
            item.put("unreadCount", unreadCount);
            item.put("sessionId", session == null ? null : session.getId());
            item.put("lastTime", session == null ? null : session.getLastTime());
            item.put("lastMsg", session == null ? null : session.getLastMsg());
            item.put("jiaoyouxinxiId", latestJiaoyou == null ? null : latestJiaoyou.getId());
            item.put("jiaoyoumude", latestJiaoyou == null ? null : latestJiaoyou.getJiaoyoumude());
            item.put("gerenjianjie", latestJiaoyou == null ? null : latestJiaoyou.getGerenjianjie());
            item.put("friendAddtime", relation.getAddtime());
            list.add(item);
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("list", list);
        data.put("total", page.getTotal());
        return R.ok().put("data", data);
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return R.error("未登录");
        }
        if (ids == null || ids.length == 0) {
            return R.ok();
        }
        for (Long id : ids) {
            HaoyoushenqingEntity entity = haoyoushenqingService.selectById(id);
            if (entity != null && userId.equals(entity.getToUserid())) {
                haoyoushenqingService.deleteById(id);
            }
        }
        return R.ok();
    }

    @RequestMapping("/friendDelete")
    public R friendDelete(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return R.error("未登录");
        }
        Long friendUserId = body.get("friendUserId") == null ? null : Long.valueOf(body.get("friendUserId").toString());
        if (friendUserId == null) {
            return R.error("缺少好友信息");
        }
        haoyouService.delete(new EntityWrapper<HaoyouEntity>().eq("userid", userId).eq("friend_userid", friendUserId));
        haoyouService.delete(new EntityWrapper<HaoyouEntity>().eq("userid", friendUserId).eq("friend_userid", userId));
        return R.ok();
    }

    @RequestMapping("/audit")
    public R audit(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Long id = body.get("id") == null ? null : Long.valueOf(body.get("id").toString());
        String status = body.get("status") == null ? null : body.get("status").toString();
        String reply = body.get("reply") == null ? null : body.get("reply").toString();
        if (id == null || status == null) {
            return R.error("参数错误");
        }
        HaoyoushenqingEntity entity = haoyoushenqingService.selectById(id);
        if (entity == null) {
            return R.error("记录不存在");
        }
        if (!"待处理".equals(entity.getStatus())) {
            return R.error("已处理");
        }
        entity.setStatus(status);
        entity.setReply(reply);
        haoyoushenqingService.updateById(entity);
        if ("同意".equals(status)) {
            Long fromUserId = entity.getFromUserid();
            Long toUserId = entity.getToUserid();
            HaoyouEntity f1 = haoyouService.selectOne(
                    new EntityWrapper<HaoyouEntity>().eq("userid", fromUserId).eq("friend_userid", toUserId));
            if (f1 == null) {
                HaoyouEntity a = new HaoyouEntity<Object>();
                a.setId(new Date().getTime() + (long) Math.floor(Math.random() * 1000));
                a.setUserid(fromUserId);
                a.setFriendUserid(toUserId);
                haoyouService.insert(a);
            }
            HaoyouEntity f2 = haoyouService.selectOne(
                    new EntityWrapper<HaoyouEntity>().eq("userid", toUserId).eq("friend_userid", fromUserId));
            if (f2 == null) {
                HaoyouEntity b = new HaoyouEntity<Object>();
                b.setId(new Date().getTime() + (long) Math.floor(Math.random() * 1000));
                b.setUserid(toUserId);
                b.setFriendUserid(fromUserId);
                haoyouService.insert(b);
            }
        }
        return R.ok();
    }
}
