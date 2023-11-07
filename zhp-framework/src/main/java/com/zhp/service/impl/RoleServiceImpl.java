package com.zhp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhp.Utils.BeanCopyUtils;
import com.zhp.contents.SystemConstants;
import com.zhp.domain.result.ResponseResult;
import com.zhp.domain.vo.PageVo;
import com.zhp.domain.vo.adminVo.RoleVo;
import com.zhp.mapper.RoleMapper;
import com.zhp.service.RoleService;
import org.springframework.stereotype.Service;
import com.zhp.domain.entity.Role;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-10-24 16:58:44
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult userList(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        queryWrapper.eq(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        Page<Role> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<RoleVo> roleVo = BeanCopyUtils.copyBeanList(page.getRecords(),RoleVo.class);

        return ResponseResult.okResult(new PageVo(roleVo,page.getTotal()));
    }

    @Override
    public ResponseResult roleAllList() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, SystemConstants.ROLE_STATUS_NORMAL);
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(list(queryWrapper), RoleVo.class);
        return ResponseResult.okResult(roleVos);
    }
}

