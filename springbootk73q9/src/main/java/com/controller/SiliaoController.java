package com.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.SiliaoHuihuaEntity;
import com.entity.SiliaoXiaoxiEntity;
import com.entity.HaoyoushenqingEntity;
import com.entity.YonghuEntity;
import com.service.HaoyoushenqingService;
import com.service.SiliaoHuihuaService;
import com.service.SiliaoXiaoxiService;
import com.service.YonghuService;
import com.utils.R;
import com.baomidou.mybatisplus.plugins.Page;

@RestController
@RequestMapping("/siliao")
public class SiliaoController {

    @Autowired
    private SiliaoHuihuaService siliaoHuihuaService;

    @Autowired
    private SiliaoXiaoxiService siliaoXiaoxiService;

    @Autowired
    private YonghuService yonghuService;

    @Autowired
    private HaoyoushenqingService haoyoushenqingService;

    private long idGen() {
        return new Date().getTime() + (long) Math.floor(Math.random() * 1000);
    }

    private SiliaoHuihuaEntity<?> getOrCreateSession(Long a, Long b) {
        Long user1 = a < b ? a : b;
        Long user2 = a < b ? b : a;
        SiliaoHuihuaEntity<?> exist = siliaoHuihuaService.selectOne(
                new EntityWrapper<SiliaoHuihuaEntity>().eq("user1_id", user1).eq("user2_id", user2));
        if (exist != null) return exist;
        SiliaoHuihuaEntity<?> s = new SiliaoHuihuaEntity<Object>();
        s.setId(idGen());
        s.setUser1Id(user1);
        s.setUser2Id(user2);
        s.setLastTime(new Date());
        siliaoHuihuaService.insert(s);
        return s;
    }

    @RequestMapping("/open")
    public R open(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Long me = (Long) request.getSession().getAttribute("userId");
        if (me == null) return R.error("未登录");
        String toZhanghao = body.get("toZhanghao") == null ? null : body.get("toZhanghao").toString();
        if (toZhanghao == null) return R.error("缺少对方账号");
        YonghuEntity<?> peer = yonghuService.selectOne(new EntityWrapper<YonghuEntity>().eq("zhanghao", toZhanghao));
        if (peer == null) return R.error("对方不存在");
        if (peer.getId().equals(me)) return R.error("不能和自己私聊");
        SiliaoHuihuaEntity<?> session = getOrCreateSession(me, peer.getId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("sessionId", session.getId());
        data.put("peerUserId", peer.getId());
        data.put("peerZhanghao", peer.getZhanghao());
        data.put("peerXingming", peer.getXingming());
        data.put("peerTouxiang", peer.getTouxiang());
        return R.ok().put("data", data);
    }

    @RequestMapping("/send")
    public R send(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Long me = (Long) request.getSession().getAttribute("userId");
        if (me == null) return R.error("未登录");
        String content = body.get("content") == null ? null : body.get("content").toString();
        if (content == null || content.trim().length() == 0) return R.error("内容不能为空");
        Long sessionId = body.get("sessionId") == null ? null : Long.valueOf(body.get("sessionId").toString());
        Long peerUserId = body.get("peerUserId") == null ? null : Long.valueOf(body.get("peerUserId").toString());
        if (peerUserId == null) return R.error("缺少对方");

        SiliaoHuihuaEntity<?> session;
        if (sessionId != null) {
            session = siliaoHuihuaService.selectById(sessionId);
            if (session == null) {
                session = getOrCreateSession(me, peerUserId);
            }
        } else {
            session = getOrCreateSession(me, peerUserId);
            sessionId = session.getId();
        }

        SiliaoXiaoxiEntity<?> msg = new SiliaoXiaoxiEntity<Object>();
        msg.setId(idGen());
        msg.setSessionId(sessionId);
        msg.setFromUserid(me);
        msg.setToUserid(peerUserId);
        msg.setContent(content);
        msg.setIsRead(0);
        siliaoXiaoxiService.insert(msg);

        session.setLastMsg(content.length() > 100 ? content.substring(0, 100) : content);
        session.setLastTime(new Date());
        siliaoHuihuaService.updateById(session);

        return R.ok().put("data", msg);
    }

    @RequestMapping("/unreadCount")
    public R unreadCount(HttpServletRequest request) {
        Long me = (Long) request.getSession().getAttribute("userId");
        if (me == null) return R.ok().put("count", 0);
        int count = siliaoXiaoxiService.selectCount(
                new EntityWrapper<SiliaoXiaoxiEntity>().eq("to_userid", me).eq("is_read", 0));
        return R.ok().put("count", count);
    }

    @RequestMapping("/sessions")
    public R sessions(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        Long me = (Long) request.getSession().getAttribute("userId");
        if (me == null) return R.error("未登录");

        int pageNum = params.get("page") == null ? 1 : Integer.parseInt(params.get("page").toString());
        int limit = params.get("limit") == null ? 20 : Integer.parseInt(params.get("limit").toString());
        Page<SiliaoHuihuaEntity> page = new Page<SiliaoHuihuaEntity>(pageNum, limit);
        com.baomidou.mybatisplus.mapper.Wrapper<SiliaoHuihuaEntity> ew = new EntityWrapper<SiliaoHuihuaEntity>()
                .andNew().eq("user1_id", me).or().eq("user2_id", me).orderBy("last_time", false);
        List<SiliaoHuihuaEntity> records = siliaoHuihuaService.selectPage(page, ew).getRecords();

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (SiliaoHuihuaEntity s : records) {
            Long peerId = me.equals(s.getUser1Id()) ? s.getUser2Id() : s.getUser1Id();
            YonghuEntity<?> peer = yonghuService.selectById(peerId);
            int unread = siliaoXiaoxiService.selectCount(
                    new EntityWrapper<SiliaoXiaoxiEntity>().eq("session_id", s.getId()).eq("to_userid", me).eq("is_read", 0));
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("sessionId", s.getId());
            m.put("peerUserId", peerId);
            m.put("peerZhanghao", peer == null ? null : peer.getZhanghao());
            m.put("peerXingming", peer == null ? null : peer.getXingming());
            m.put("peerTouxiang", peer == null ? null : peer.getTouxiang());
            m.put("lastMsg", s.getLastMsg());
            m.put("lastTime", s.getLastTime());
            m.put("unreadCount", unread);
            list.add(m);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("list", list);
        data.put("total", page.getTotal());
        return R.ok().put("data", data);
    }

    @RequestMapping("/history")
    public R history(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        Long me = (Long) request.getSession().getAttribute("userId");
        if (me == null) return R.error("未登录");
        Long sessionId = params.get("sessionId") == null ? null : Long.valueOf(params.get("sessionId").toString());
        if (sessionId == null) return R.error("缺少会话");

        int pageNum = params.get("page") == null ? 1 : Integer.parseInt(params.get("page").toString());
        int limit = params.get("limit") == null ? 50 : Integer.parseInt(params.get("limit").toString());
        Page<SiliaoXiaoxiEntity> page = new Page<SiliaoXiaoxiEntity>(pageNum, limit);
        com.baomidou.mybatisplus.mapper.Wrapper<SiliaoXiaoxiEntity> ew = new EntityWrapper<SiliaoXiaoxiEntity>()
                .eq("session_id", sessionId).orderBy("addtime", true);
        List<SiliaoXiaoxiEntity> records = siliaoXiaoxiService.selectPage(page, ew).getRecords();

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (SiliaoXiaoxiEntity msg : records) {
            YonghuEntity<?> from = yonghuService.selectById(msg.getFromUserid());
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("id", msg.getId());
            m.put("sessionId", msg.getSessionId());
            m.put("fromUserid", msg.getFromUserid());
            m.put("toUserid", msg.getToUserid());
            m.put("content", msg.getContent());
            m.put("isRead", msg.getIsRead());
            m.put("addtime", msg.getAddtime());
            m.put("fromXingming", from == null ? null : from.getXingming());
            m.put("fromTouxiang", from == null ? null : from.getTouxiang());
            list.add(m);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("list", list);
        data.put("total", page.getTotal());
        return R.ok().put("data", data);
    }

    @RequestMapping("/markRead")
    public R markRead(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Long me = (Long) request.getSession().getAttribute("userId");
        if (me == null) return R.error("未登录");
        Long sessionId = body.get("sessionId") == null ? null : Long.valueOf(body.get("sessionId").toString());
        if (sessionId == null) return R.error("缺少会话");
        List<SiliaoXiaoxiEntity> list = siliaoXiaoxiService.selectList(
                new EntityWrapper<SiliaoXiaoxiEntity>().eq("session_id", sessionId).eq("to_userid", me).eq("is_read", 0));
        for (SiliaoXiaoxiEntity m : list) {
            m.setIsRead(1);
            siliaoXiaoxiService.updateById(m);
        }
        return R.ok();
    }

    @RequestMapping("/unreadSummary")
    public R unreadSummary(HttpServletRequest request) {
        Long me = (Long) request.getSession().getAttribute("userId");
        if (me == null) {
            return R.ok()
                    .put("friendUnreadCount", 0)
                    .put("chatUnreadCount", 0)
                    .put("totalUnreadCount", 0);
        }
        int friendUnreadCount = haoyoushenqingService.selectCount(
                new EntityWrapper<HaoyoushenqingEntity>().eq("to_userid", me).eq("status", "待处理"));
        int chatUnreadCount = siliaoXiaoxiService.selectCount(
                new EntityWrapper<SiliaoXiaoxiEntity>().eq("to_userid", me).eq("is_read", 0));
        return R.ok()
                .put("friendUnreadCount", friendUnreadCount)
                .put("chatUnreadCount", chatUnreadCount)
                .put("totalUnreadCount", friendUnreadCount + chatUnreadCount);
    }

    @RequestMapping("/unreadList")
    public R unreadList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        Long me = (Long) request.getSession().getAttribute("userId");
        if (me == null) {
            return R.error("未登录");
        }
        int limit = params.get("limit") == null ? 10 : Integer.parseInt(params.get("limit").toString());
        if (limit <= 0) {
            limit = 10;
        }
        if (limit > 50) {
            limit = 50;
        }

        List<HaoyoushenqingEntity> friendRawList = haoyoushenqingService.selectList(
                new EntityWrapper<HaoyoushenqingEntity>()
                        .eq("to_userid", me)
                        .eq("status", "待处理")
                        .orderBy("addtime", false)
                        .last("limit " + limit));
        List<Map<String, Object>> friendList = new ArrayList<Map<String, Object>>();
        for (HaoyoushenqingEntity req : friendRawList) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", req.getId());
            item.put("fromUserid", req.getFromUserid());
            item.put("fromZhanghao", req.getFromZhanghao());
            item.put("fromXingming", req.getFromXingming());
            item.put("addtime", req.getAddtime());
            item.put("status", req.getStatus());
            friendList.add(item);
        }

        List<SiliaoXiaoxiEntity> chatRawList = siliaoXiaoxiService.selectList(
                new EntityWrapper<SiliaoXiaoxiEntity>()
                        .eq("to_userid", me)
                        .eq("is_read", 0)
                        .orderBy("addtime", false)
                        .last("limit " + limit));
        List<Map<String, Object>> chatList = new ArrayList<Map<String, Object>>();
        for (SiliaoXiaoxiEntity msg : chatRawList) {
            YonghuEntity<?> sender = yonghuService.selectById(msg.getFromUserid());
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", msg.getId());
            item.put("sessionId", msg.getSessionId());
            item.put("fromUserid", msg.getFromUserid());
            item.put("fromZhanghao", sender == null ? null : sender.getZhanghao());
            item.put("fromXingming", sender == null ? null : sender.getXingming());
            item.put("fromTouxiang", sender == null ? null : sender.getTouxiang());
            item.put("content", msg.getContent());
            item.put("addtime", msg.getAddtime());
            chatList.add(item);
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("friendList", friendList);
        data.put("chatList", chatList);
        return R.ok().put("data", data);
    }
}
