package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 好友申请
 * 数据库通用操作实体类（普通增删改查）
 */
@TableName("haoyoushenqing")
public class HaoyoushenqingEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	public HaoyoushenqingEntity() {
	}

	public HaoyoushenqingEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@TableId
	private Long id;

	private Long fromUserid;

	private String fromZhanghao;

	private String fromXingming;

	private Long toUserid;

	private String toZhanghao;

	private String toXingming;

	private String status;

	private String reply;

	private Long refJiaoyouxinxiId;

	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFromUserid() {
		return fromUserid;
	}

	public void setFromUserid(Long fromUserid) {
		this.fromUserid = fromUserid;
	}

	public String getFromZhanghao() {
		return fromZhanghao;
	}

	public void setFromZhanghao(String fromZhanghao) {
		this.fromZhanghao = fromZhanghao;
	}

	public String getFromXingming() {
		return fromXingming;
	}

	public void setFromXingming(String fromXingming) {
		this.fromXingming = fromXingming;
	}

	public Long getToUserid() {
		return toUserid;
	}

	public void setToUserid(Long toUserid) {
		this.toUserid = toUserid;
	}

	public String getToZhanghao() {
		return toZhanghao;
	}

	public void setToZhanghao(String toZhanghao) {
		this.toZhanghao = toZhanghao;
	}

	public String getToXingming() {
		return toXingming;
	}

	public void setToXingming(String toXingming) {
		this.toXingming = toXingming;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public Long getRefJiaoyouxinxiId() {
		return refJiaoyouxinxiId;
	}

	public void setRefJiaoyouxinxiId(Long refJiaoyouxinxiId) {
		this.refJiaoyouxinxiId = refJiaoyouxinxiId;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
}

