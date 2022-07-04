package com.mengtu.kaichi.serviceimpl.common.verify;

/**
 * 鉴权请求接口
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/23 22:03
 */
public interface VerifyRequest {
    /**
     * 验权 ID
     *
     * @return
     */
    String getVerifyUserId();
}
