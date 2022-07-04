package com.mengtu.kaichi.linkshow.dal.model;

import com.mengtu.util.common.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 实体基础类
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/16 09:25
 */
@MappedSuperclass
public abstract class BaseDO extends ToString {

    private static final long serialVersionUID = -2418976828799135216L;
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "gmt_create", nullable = false)
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified", nullable = false)
    private Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}
