package com.mengtu.kaichi.serviceimpl.common;

import com.mengtu.util.common.ToString;

/**
 * 操作上下文
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/22 11:20
 */
public class OperateContext extends ToString {

    private static final long serialVersionUID = -3393208008630233175L;

    private String operateIp;

    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp;
    }

}
