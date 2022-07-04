package com.mengtu.kaichi.user.manager.impl;

import com.mengtu.kaichi.user.dal.service.PermRepoService;
import com.mengtu.kaichi.user.manager.PermManager;
import com.mengtu.kaichi.user.model.basic.perm.PermBO;
import com.mengtu.kaichi.user.model.basic.perm.UserPermRelationBO;
import com.mengtu.kaichi.user.request.PermManageRequest;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限管理器实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 09:42
 */
@Service
public class PermManagerImpl implements PermManager {

    @Resource
    private PermRepoService permRepoService;

    @Override
    public PermBO createPerm(PermManageRequest request) {
        return permRepoService.createPerm(request.getPermBO());
    }

    @Override
    public void batchUsersBindPerms(PermManageRequest request) {
        AssertUtil.assertNotNull(request);
        AssertUtil.assertNotNull(request.getPermBO());
        AssertUtil.assertStringNotBlank(request.getPermBO().getPermId());
        AssertUtil.assertNotNull(request.getUserIds());
        permRepoService.usersBindPerm(request.getUserIds(), request.getPermBO().getPermId());
    }

    @Override
    public void batchUsersUnbindPerms(PermManageRequest request) {
        AssertUtil.assertNotNull(request);
        AssertUtil.assertNotNull(request.getPermBO());
        AssertUtil.assertNotNull(request.getUserIds());
        AssertUtil.assertStringNotBlank(request.getPermBO().getPermId());
        permRepoService.usersUnbindPerm(request.getUserIds(), request.getPermBO().getPermId());
    }

    @Override
    public void detachAllUsers(String permId) {
        permRepoService.detachAllUsers(permId);
    }

    @Override
    public List<String> getPermUsers(String permId) {
        return CollectionUtil.toStream(permRepoService.getUserPermRelationsOrderByCreate(permId))
                .map(UserPermRelationBO::getUserId).collect(Collectors.toList());
    }
}
