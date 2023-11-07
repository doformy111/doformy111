package com.zhp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhp.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-24 16:58:44
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}
