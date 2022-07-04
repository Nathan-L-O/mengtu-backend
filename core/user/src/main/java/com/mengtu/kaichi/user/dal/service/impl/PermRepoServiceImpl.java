package com.mengtu.kaichi.user.dal.service.impl;

import com.mengtu.kaichi.user.dal.convert.EntityConverter;
import com.mengtu.kaichi.user.dal.convert.RelationConverter;
import com.mengtu.kaichi.user.dal.model.perm.*;
import com.mengtu.kaichi.user.dal.repo.perm.*;
import com.mengtu.kaichi.user.dal.service.PermRepoService;
import com.mengtu.kaichi.user.idfactory.BizIdFactory;
import com.mengtu.kaichi.user.model.basic.perm.PermBO;
import com.mengtu.kaichi.user.model.basic.perm.RolePermRelationBO;
import com.mengtu.kaichi.user.model.basic.perm.UserBO;
import com.mengtu.kaichi.user.model.basic.perm.UserPermRelationBO;
import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import com.mengtu.util.tools.LoggerUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 权限仓储服务实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 09:45
 */
@Service
public class PermRepoServiceImpl implements PermRepoService {

    private final Logger LOGGER = LoggerFactory.getLogger(PermRepoServiceImpl.class);

    @Resource
    private PermDORepo permDORepo;

    @Resource
    private RoleDORepo roleDORepo;

    @Resource
    private UserDORepo userDORepo;

    @Resource
    private UserPermRelationDORepo userPermRelationDORepo;

    @Autowired
    private RolePermRelationDORepo rolePermRelationDORepo;

    /**
     * id工厂
     */
    @Autowired
    private BizIdFactory userBizIdFactory;


    @Override
    public PermBO createPerm(PermBO permBO) {
        if (StringUtils.isBlank(permBO.getPermId())) {
            permBO.setPermId(userBizIdFactory.getPermId());
        }
        return EntityConverter.convert(permDORepo.save(EntityConverter.convert(permBO)));
    }

    @Override
    public List<PermBO> queryPermsByPermIds(List<String> permIds) {
        List<PermDO> permDOList = permDORepo.findAllByPermIdIn(permIds);
        return CollectionUtil.toStream(permDOList)
                .filter(Objects::nonNull)
                .map(EntityConverter::convert).collect(Collectors.toList());
    }

    @Override
    public List<PermBO> queryPermByRoleId(String roleId) {
        // 获取映射关系
        List<RolePermRelationDO> rolePermRelations = rolePermRelationDORepo.findAllByRoleId(roleId);
        // 从映射关系抽出权限id
        List<String> permIds = CollectionUtil.toStream(rolePermRelations)
                .filter(Objects::nonNull)
                .map(RolePermRelationDO::getPermId)
                .collect(Collectors.toList());
        // 获取对应权限实体
        return queryPermsByPermIds(permIds);
    }

    @Override
    public List<PermBO> batchQueryPermByRoleId(List<String> roleIds) {
        // 获取映射关系
        List<RolePermRelationDO> rolePermRelations = rolePermRelationDORepo.findAllByRoleIdIn(roleIds);
        // 从映射关系抽出权限id
        List<String> permIds = CollectionUtil.toStream(rolePermRelations)
                .filter(Objects::nonNull)
                .map(RolePermRelationDO::getPermId)
                .collect(Collectors.toList());
        // 获取对应权限实体
        return queryPermsByPermIds(permIds);
    }

    @Override
    public List<PermBO> queryPermByUserId(String userId) {
        // 获取映射关系
        List<UserPermRelationDO> userPermRelations = userPermRelationDORepo.findAllByUserId(userId);
        // 从映射关系抽出权限id
        List<String> permIds = CollectionUtil.toStream(userPermRelations)
                .filter(Objects::nonNull)
                .map(UserPermRelationDO::getPermId)
                .collect(Collectors.toList());
        // 获取对应权限实体
        return queryPermsByPermIds(permIds);
    }

    @Override
    public List<PermBO> roleBindPerms(String roleId, List<String> permIds) {
        // 获取用户信息
        RoleDO roleDO = roleDORepo.findByRoleId(roleId);
        AssertUtil.assertNotNull(roleDO, "角色不存在");

        // 获取 permIds 对应的权限信息
        List<PermDO> permDOList = permDORepo.findAllByPermIdIn(permIds);

        // 绑定的权限
        List<PermBO> bindPerms = CollectionUtil.toStream(permDOList)
                .filter(Objects::nonNull)
                .map(EntityConverter::convert).collect(Collectors.toList());

        // permIds 有查不到的就属于异常
        if (permDOList.size() != permIds.size()) {
            LoggerUtil.error(LOGGER, "绑定的权限id不存在 permIds={0}, permDOList={1}", permIds, permDOList);
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), "绑定的权限id不存在");
        }

        // 查询已经绑定的权限
        List<String> roleBoundPermIds = CollectionUtil.toStream(rolePermRelationDORepo.findAllByRoleId(roleId))
                .filter(Objects::nonNull).map(RolePermRelationDO::getPermId)
                .collect(Collectors.toList());

        // 存在已绑定的权限,需要特殊处理 必须要迭代器删除不然会并发修改问题
        if (!CollectionUtil.isEmpty(roleBoundPermIds)) {
            // 构建迭代器
            Iterator<PermDO> permIterator = permDOList.iterator();
            while (permIterator.hasNext()) {
                PermDO perm = permIterator.next();
                for (String boundId : roleBoundPermIds) {
                    // 是已绑的权限 就从里面移除
                    if (StringUtils.equals(boundId, perm.getPermId())) {
                        permIterator.remove();
                    }
                }
            }
        }

        // 构建关联关系实体
        List<RolePermRelationDO> relations = new ArrayList<>();
        for (PermDO permDO : permDOList) {
            RolePermRelationBO relation = new RolePermRelationBO();
            relation.setPermId(permDO.getPermId());
            relation.setRoleId(roleId);
            // 通过 id 工厂构建关联id
            relation.setRolePermId(userBizIdFactory.getRoleUserRelationId(roleId, permDO.getPermId()));
            relations.add(RelationConverter.convert(relation));
        }
        // 绑定
        rolePermRelationDORepo.saveAll(relations);

        return bindPerms;
    }

    @Override
    @Transactional
    public void roleUnbindPerms(String roleId, List<String> permIds) {
        // 获取用户信息
        RoleDO roleDO = roleDORepo.findByRoleId(roleId);
        AssertUtil.assertNotNull(roleDO, "角色不存在");

        // 获取 permIds 对应的权限信息
        List<PermDO> permDOList = permDORepo.findAllByPermIdIn(permIds);


        // permIds 有查不到的就属于异常
        if (permDOList.size() != permIds.size()) {
            LoggerUtil.warn(LOGGER, "解绑的权限id不存在 permIds={0}, permDOList={1}", permIds, permDOList);
        }
        rolePermRelationDORepo.deleteByRoleIdAndPermIdIn(roleId, permIds);
    }

    @Override
    public List<PermBO> userBindPerms(String userId, List<String> permIds) {
        // 获取用户信息
        UserDO userDO = userDORepo.findByUserId(userId);
        AssertUtil.assertNotNull(userDO, "用户不存在");

        // 获取 permIds 对应的权限信息
        List<PermDO> permDOList = permDORepo.findAllByPermIdIn(permIds);

        // 绑定的权限
        List<PermBO> bindPerms = CollectionUtil.toStream(permDOList)
                .filter(Objects::nonNull)
                .map(EntityConverter::convert).collect(Collectors.toList());

        // permIds 有查不到的就属于异常
        if (permDOList.size() != permIds.size()) {
            LoggerUtil.error(LOGGER, "绑定的权限id不存在 permIds={0}, permDOList={1}", permIds, permDOList);
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), "绑定的权限id不存在");
        }

        // 查询已经绑定的权限
        List<String> userBoundPermIds = CollectionUtil.toStream(userPermRelationDORepo.findAllByUserId(userId))
                .filter(Objects::nonNull).map(UserPermRelationDO::getPermId)
                .collect(Collectors.toList());

        // 存在已绑定的权限,需要特殊处理 必须要迭代器删除不然会并发修改问题
        if (!CollectionUtil.isEmpty(userBoundPermIds)) {
            // 构建迭代器
            Iterator<PermDO> permIterator = permDOList.iterator();
            while (permIterator.hasNext()) {
                PermDO perm = permIterator.next();
                for (String boundId : userBoundPermIds) {
                    // 是已绑的角色 就从里面移除
                    if (StringUtils.equals(boundId, perm.getPermId())) {
                        LoggerUtil.warn(LOGGER, "用户重复绑定权限 userId={0}, perm={1}", userId, perm);
                        permIterator.remove();
                    }
                }
            }
        }

        // 构建关联关系实体
        List<UserPermRelationDO> relations = new ArrayList<>();
        for (PermDO permDO : permDOList) {
            UserPermRelationBO relation = new UserPermRelationBO();
            relation.setPermId(permDO.getPermId());
            relation.setUserId(userId);
            // 通过 idfactory 工厂构建关联id
            relation.setUserPermId(userBizIdFactory.getUserPermRelationId(userId, permDO.getPermId()));
            relations.add(RelationConverter.convert(relation));
        }
        // 绑定
        userPermRelationDORepo.saveAll(relations);

        return bindPerms;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userUnbindPerms(String userId, List<String> permIds) {
        // 获取用户信息
        UserDO userDO = userDORepo.findByUserId(userId);
        AssertUtil.assertNotNull(userDO, "用户不存在");

        // 获取 permIds 对应的权限信息
        List<PermDO> permDOList = permDORepo.findAllByPermIdIn(permIds);

        // permIds 有查不到的就属于异常
        if (permDOList.size() != permIds.size()) {
            LoggerUtil.error(LOGGER, "绑定的权限id不存在 permIds={0}, permDOList={1}", permIds, permDOList);
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), "绑定的权限id不存在");
        }

        userPermRelationDORepo.deleteByUserIdAndPermIdIn(userId, permIds);

    }

    @Override
    public List<UserBO> usersBindPerm(List<String> userIds, String permId) {
        // 获取权限信息
        PermDO permDO = permDORepo.findByPermId(permId);
        AssertUtil.assertNotNull(permDO, "权限不存在");


        List<UserDO> userDOList = userDORepo.findAllByUserIdIn(userIds);
        if (userDOList.size() != userIds.size()) {
            LoggerUtil.error(LOGGER, "绑定的用户id不存在 userIds={0}, userList={1}", userIds, userDOList);
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), "需要绑定的用户不存在");
        }

        // 绑定的用户角色
        List<UserBO> bindUsers = CollectionUtil.toStream(userDOList)
                .filter(Objects::nonNull)

                .map(EntityConverter::convert).collect(Collectors.toList());

        // 查询已经绑定的权限
        List<String> boundUserIds = CollectionUtil.toStream(userPermRelationDORepo.findAllByPermIdOrderByGmtCreate(permId))
                .filter(Objects::nonNull).map(UserPermRelationDO::getUserId)
                .collect(Collectors.toList());

        // 存在已绑定的角色,需要特殊处理 必须要迭代器删除不然会并发修改问题
        CollectionUtil.removeDuplicate(userDOList, boundUserIds, UserDO::getUserId);

        // 构建关联关系实体
        List<UserPermRelationDO> relations = new ArrayList<>();
        for (UserDO userDO : userDOList) {
            UserPermRelationBO relation = new UserPermRelationBO();
            relation.setPermId(permId);
            relation.setUserId(userDO.getUserId());
            // 通过 idfactory 工厂构建关联id
            relation.setUserPermId(userBizIdFactory.getUserPermRelationId(permId, userDO.getUserId()));
            relations.add(RelationConverter.convert(relation));
        }
        // 绑定
        userPermRelationDORepo.saveAll(relations);

        return bindUsers;
    }

    @Override
    @Transactional
    public void usersUnbindPerm(List<String> userIds, String permId) {
        // 获取权限信息
        PermDO permDO = permDORepo.findByPermId(permId);
        AssertUtil.assertNotNull(permDO, "权限不存在");


        List<UserDO> userDOList = userDORepo.findAllByUserIdIn(userIds);
        if (userDOList.size() != userIds.size()) {
            LoggerUtil.error(LOGGER, "绑定的用户id不存在 userIds={0}, userList={1}", userIds, userDOList);
        }
        userPermRelationDORepo.deleteByPermIdAndUserIdIn(permId, userIds);
    }

    @Override
    @Transactional
    public void detachAllRoles(String permId) {
        // 获取权限信息
        PermDO permDO = permDORepo.findByPermId(permId);
        AssertUtil.assertNotNull(permDO, "权限不存在");
        rolePermRelationDORepo.deleteByPermId(permId);
    }

    @Override
    @Transactional
    public void detachAllUsers(String permId) {
        // 获取权限信息
        PermDO permDO = permDORepo.findByPermId(permId);
        AssertUtil.assertNotNull(permDO, "权限不存在");
        userPermRelationDORepo.deleteByPermId(permId);
    }

    @Override
    public void deletePerm(String permId) {
        AssertUtil.assertStringNotBlank(permId, "权限id不能为空");
        permDORepo.deleteByPermId(permId);
    }

    @Override
    public boolean verifyUserPermRelationByPermId(String userId, List<String> permIds) {
        // 获取权限信息
        List<PermDO> perms = permDORepo.findAllByPermIdIn(permIds);
        boolean permsExist = perms.size() == permIds.size();
        AssertUtil.assertTrue(permsExist, "权限不存在");

        // 逐一鉴权
        for (String permId : permIds) {
            if (userPermRelationDORepo.findByUserIdAndPermId(userId, permId) == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean verifyUserPermRelationByPermType(String userId, List<String> permTypes) {
        // 获取权限信息
        List<PermDO> perms = permDORepo.findAllByPermTypeIn(permTypes);
        boolean permsExist = perms.size() == permTypes.size();
        AssertUtil.assertTrue(permsExist, "权限不存在");

        // 逐一鉴权
        for (PermDO perm : perms) {
            if (userPermRelationDORepo.findByUserIdAndPermId(userId, perm.getPermId()) == null) {
                return false;
            }
        }
        return true;

    }

    @Override
    public boolean verifyRolePermRelationByPermId(String roleId, List<String> permIds) {
        // 获取权限信息
        List<PermDO> perms = permDORepo.findAllByPermIdIn(permIds);
        boolean permsExist = perms.size() == permIds.size();
        AssertUtil.assertTrue(permsExist, "权限不存在");

        // 逐一鉴权
        for (String permId : permIds) {
            if (rolePermRelationDORepo.findByRoleIdAndPermId(roleId, permId) == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean verifyRolePermRelationByPermType(String roleId, List<String> permTypes) {
        // 获取权限信息
        List<PermDO> perms = permDORepo.findAllByPermTypeIn(permTypes);
        boolean permsExist = perms.size() == permTypes.size();
        AssertUtil.assertTrue(permsExist, "权限不存在");

        // 逐一鉴权
        for (PermDO perm : perms) {
            if (rolePermRelationDORepo.findByRoleIdAndPermId(roleId, perm.getPermId()) == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public PermBO initPerm(PermBO permBO) {
        PermDO permDO = permDORepo.findByPermType(permBO.getPermType());
        if (permDO == null) {
            if (StringUtils.isBlank(permBO.getPermId())) {
                permBO.setPermId(userBizIdFactory.getPermId());
            }
            permDO = permDORepo.save(EntityConverter.convert(permBO));
        }
        return EntityConverter.convert(permDO);
    }

    @Override
    public PermBO initFinancePerm(PermBO permBO) {
        PermDO permDO = permDORepo.findByExtInfo(EntityConverter.convert(permBO).getExtInfo());
        if (permDO == null) {
            if (StringUtils.isBlank(permBO.getPermId())) {
                permBO.setPermId(userBizIdFactory.getPermId());
            }
            permDO = permDORepo.save(EntityConverter.convert(permBO));
        }
        return EntityConverter.convert(permDO);
    }

    @Override
    public List<UserPermRelationBO> getUserPermRelationsOrderByCreate(String permId) {
        return CollectionUtil.toStream(userPermRelationDORepo.findAllByPermIdOrderByGmtCreate(permId))
                .filter(Objects::nonNull)
                .map(RelationConverter::convert)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<PermBO> roleBindPermsUnbindNo(String roleId, List<String> permIds) {
        // 获取用户信息
        RoleDO roleDO = roleDORepo.findByRoleId(roleId);
        AssertUtil.assertNotNull(roleDO, "角色不存在");

        // 获取 permIds 对应的权限信息
        List<PermDO> permDOList = permDORepo.findAllByPermIdIn(permIds);

        // 绑定的权限
        List<PermBO> bindPerms = CollectionUtil.toStream(permDOList)
                .filter(Objects::nonNull)
                .map(EntityConverter::convert).collect(Collectors.toList());

        // permIds 有查不到的就属于异常
        if (permDOList.size() != permIds.size()) {
            LoggerUtil.error(LOGGER, "绑定的权限id不存在 permIds={0}, permDOList={1}", permIds, permDOList);
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), "绑定的权限id不存在");
        }

        // 查询已经绑定的权限
        List<String> roleBoundPermIds = CollectionUtil.toStream(rolePermRelationDORepo.findAllByRoleId(roleId))
                .filter(Objects::nonNull).map(RolePermRelationDO::getPermId)
                .collect(Collectors.toList());

        List<String> permInRoleAlreadyBindPermIds = new ArrayList<>();
        // 存在已绑定的权限,需要特殊处理 必须要迭代器删除不然会并发修改问题
        if (!CollectionUtil.isEmpty(roleBoundPermIds)) {
            // 构建迭代器
            Iterator<String> rolePermIterator = roleBoundPermIds.iterator();
            Iterator<PermDO> permIterator = permDOList.iterator();
            while (permIterator.hasNext()) {
                PermDO perm = permIterator.next();
                for (String boundId : roleBoundPermIds) {
                    // 是已绑的权限 就从里面移除
                    if (StringUtils.equals(boundId, perm.getPermId())) {
                        permInRoleAlreadyBindPermIds.add(boundId);
                        permIterator.remove();
                    }
                }
            }
            //从roleBoundPermIds中去除已经绑定的权限
            while (rolePermIterator.hasNext()) {
                String permId = rolePermIterator.next();
                for (String id : permInRoleAlreadyBindPermIds) {
                    if (StringUtils.equals(id, permId)) {
                        rolePermIterator.remove();
                    }
                }
            }
        }

        if (!CollectionUtil.isEmpty(roleBoundPermIds)) {
            roleUnbindPerms(roleId, roleBoundPermIds);
        }

        // 构建关联关系实体
        List<RolePermRelationDO> relations = new ArrayList<>();
        for (PermDO permDO : permDOList) {
            RolePermRelationBO relation = new RolePermRelationBO();
            relation.setPermId(permDO.getPermId());
            relation.setRoleId(roleId);
            // 通过 id 工厂构建关联id
            relation.setRolePermId(userBizIdFactory.getRoleUserRelationId(roleId, permDO.getPermId()));
            relations.add(RelationConverter.convert(relation));
        }
        // 绑定
        rolePermRelationDORepo.saveAll(relations);
        return bindPerms;
    }

}
