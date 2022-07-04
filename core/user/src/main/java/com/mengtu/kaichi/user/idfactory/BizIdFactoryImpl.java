package com.mengtu.kaichi.user.idfactory;

import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

/**
 * 业务 ID 工厂实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:36
 */
@Service("userBizIdFactory")
public class BizIdFactoryImpl implements BizIdFactory {

    /**
     * 随机数范围规约
     */
    private static final int RANDOM_RANGE = 100000000;

    /**
     * 随机发生器
     */
    private static final Random RANDOM = new Random();

    /**
     * 生成 UserID
     * 1-16 系统时间 16位
     * 16-22 随机数 6位随机数
     * 22-26 业务码 4位业务码
     * 26-30 业务码 自定义业务码
     * 30-32 随机数 2位随机数
     *
     * @return
     */
    @Override
    public String getUserId() {
        Date now = new Date();
        return DateUtil.getShortDatesStr(now) +
                getRandNum(6) +
                IdTypeEnum.USER_ID.getBizId() +
                DateUtil.getYear(now) +
                getRandNum(2);
    }

    /**
     * 生成 UserInfoID
     * 1-16 系统时间 16位
     * 16-24 随机数 8位随机数
     * 24-28 业务码 4位业务码
     * 28-30 日
     * 30-32 随机数 2位随机数
     *
     * @return
     */
    @Override
    public String getUserInfoId(String userId) {
        Date now = new Date();
        return DateUtil.getShortDatesStr(now) +
                getRandNum(8) +
                IdTypeEnum.USER_INFO_ID.getBizId() +
                DateUtil.getDay(new Date()) +
                getLengthString(userId, 2);
    }

    /**
     * 生成 RoleID
     * 1-16 系统时间 16位
     * 16-24 随机数 8位随机数
     * 24-28 业务码 4位业务码
     * 28-32 业务码 自定义业务码
     *
     * @return
     */
    @Override
    public String getRoleId() {
        Date now = new Date();
        return DateUtil.getShortDatesStr(now) +
                getRandNum(8) +
                IdTypeEnum.ROLE_ID.getBizId() +
                DateUtil.getMonthDay(now);
    }

    /**
     * 生成 RoleUserRelationID
     * 1-16 系统时间 16位
     * 16-24 随机数 8位随机数
     * 24-28 业务码 4位业务码
     * 28-30 roleId 尾部2位
     * 30-32 userId 尾部2位
     *
     * @return
     */
    @Override
    public String getRoleUserRelationId(String roleId, String userId) {
        Date now = new Date();
        return DateUtil.getShortDatesStr(now) +
                getRandNum(8) +
                IdTypeEnum.USER_ROLE_RELATION_ID.getBizId() +
                getLengthString(roleId, 2) +
                getLengthString(userId, 2);
    }

    /**
     * 生成 PermID
     * 1-16 系统时间 16位
     * 16-24 随机数 8位随机数
     * 24-28 业务码 4位业务码
     * 28-32 业务自定义码
     *
     * @return
     */
    @Override
    public String getPermId() {
        Date now = new Date();
        return DateUtil.getShortDatesStr(now) +
                getRandNum(8) +
                IdTypeEnum.PERM_ID.getBizId() +
                DateUtil.getMonthDay(now);
    }

    /**
     * 生成 RolePermRelationID
     * 1-16 系统时间 16位
     * 16-24 随机数 8位随机数
     * 24-28 业务码 4位业务码
     * 28-30 roleId 尾部2位
     * 30-32 permId 尾部2位
     *
     * @return
     */
    @Override
    public String getRolePermRelationId(String roleId, String permId) {
        Date now = new Date();
        return DateUtil.getShortDatesStr(now) +
                getRandNum(8) +
                IdTypeEnum.ROLE_PERM_RELATION_ID.getBizId() +
                getLengthString(roleId, 2) +
                getLengthString(permId, 2);
    }

    /**
     * 生成 UserPermRelationID
     * 1-16 系统时间 16位
     * 16-24 随机数 8位随机数
     * 24-28 业务码 4位业务码
     * 28-30 userId 尾部2位
     * 30-32 permId 尾部2位
     *
     * @return
     */
    @Override
    public String getUserPermRelationId(String userId, String permId) {
        Date now = new Date();
        return DateUtil.getShortDatesStr(now) +
                getRandNum(8) +
                IdTypeEnum.USER_PERM_RELATION_ID.getBizId() +
                getLengthString(userId, 2) +
                getLengthString(permId, 2);
    }

    /**
     * 获取指定长度的随机数，不足向左补0
     *
     * @param length
     * @return
     */
    private String getRandNum(int length) {
        AssertUtil.assertTrue(length > 0, "截取长度非法");
        return getLengthString(String.valueOf(RANDOM.nextInt(RANDOM_RANGE)), length);
    }

    /**
     * 获取指定长度字符串，不足向左补 0
     *
     * @param str
     * @param length
     * @return
     */
    private String getLengthString(String str, int length) {
        String lengthString = StringUtils.right(str, length);
        if (StringUtils.isBlank(str)) {
            return getZeroString(length);
        }
        if (length > str.length()) {
            return getZeroString(length - str.length()) + str;
        }
        return lengthString;
    }

    /**
     * 获取指定长度的 0 字符串
     *
     * @param length
     * @return
     */
    private String getZeroString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("0");
        }
        return sb.toString();
    }

}
