package com.liangwj.tools2k.db.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.liangwj.tools2k.annotation.api.AComment;

/**
 * <pre>
 * 所有po的基类，为每一个表增加create_time字段，而且这个字段只在insert时可以
 * </pre>
 * 
 * @author rock
 * 
 */
@MappedSuperclass
public abstract class BasePo {

	// --------------- 主键 ---------------
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, length = 11)
	private Integer id;

	/** 创建时间 */
	@AComment("创建时间")
	@NotNull(message = "createTime不能为空")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", updatable = false, nullable = false)
	private Date createTime = new Date();

	@AComment("是否已经删除")
	@Column(name = "deleted", nullable = false) // 不能定义类型，mysql和oracle的不一样
	private boolean deleted;

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
