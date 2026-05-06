package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

@TableName("siliao_xiaoxi")
public class SiliaoXiaoxiEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	public SiliaoXiaoxiEntity() {
	}

	public SiliaoXiaoxiEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@TableId
	private Long id;

	private Long sessionId;

	private Long fromUserid;

	private Long toUserid;

	private String content;

	private Integer isRead;

	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public Long getFromUserid() {
		return fromUserid;
	}

	public void setFromUserid(Long fromUserid) {
		this.fromUserid = fromUserid;
	}

	public Long getToUserid() {
		return toUserid;
	}

	public void setToUserid(Long toUserid) {
		this.toUserid = toUserid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
}

