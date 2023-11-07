package com.zhp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhp.domain.entity.Role;
import com.zhp.domain.result.ResponseResult;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-10-24 16:58:44
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult userList(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult roleAllList();

}

