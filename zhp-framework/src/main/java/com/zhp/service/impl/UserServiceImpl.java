package com.zhp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhp.Utils.BeanCopyUtils;
import com.zhp.Utils.SecurityUtils;
import com.zhp.domain.dto.UserDto;
import com.zhp.domain.entity.Role;
import com.zhp.domain.entity.UserRole;
import com.zhp.domain.result.ResponseResult;
import com.zhp.domain.vo.PageVo;
import com.zhp.domain.vo.UserInfoVo;
import com.zhp.domain.vo.adminVo.UserVo;
import com.zhp.exception.SystemException;
import com.zhp.mapper.UserMapper;
import com.zhp.service.RoleService;
import com.zhp.service.UserRoleService;
import com.zhp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.zhp.enums.AppHttpCodeEnum;
import com.zhp.domain.entity.User;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-10-20 15:46:08
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public ResponseResult userInfo() {
    Long id = SecurityUtils.getUserId();
    User user = getById(id);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Resource
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //...
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
      LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
      queryWrapper.like(StringUtils.hasText(userName),User::getUserName,userName);
        queryWrapper.like(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        queryWrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        Page<User> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<UserVo> userVos = BeanCopyUtils.copyBeanList(page.getRecords(),UserVo.class);

        return ResponseResult.okResult(new PageVo(userVos,page.getTotal()));
    }

    @Override
    public ResponseResult addAdminUser(UserDto userDto) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.hasText(userDto.getUserName())|| getBaseMapper().selectCount(queryWrapper.eq(User::getUserName,userDto.getUserName()))>0){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(userDto.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(userDto.getEmail())|| getBaseMapper().selectCount(queryWrapper.eq(User::getEmail,userDto.getEmail()))>0){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        if(!StringUtils.hasText(userDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        } if(!StringUtils.hasText(userDto.getPhonenumber())|| getBaseMapper().selectCount(queryWrapper.eq(User::getPhonenumber,userDto.getPhonenumber()))>0){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        User user = BeanCopyUtils.copyBean(userDto,User.class);
        user.setPassword(passwordEncoder.encode(userDto.getUserName()));
        save(user);
        List<UserRole> userRoles=userDto.getRoleIds().stream()
                .map(id ->  new UserRole(user.getId(),Long.getLong(id)))
                .collect(Collectors.toList());
        userRoles.stream()
                .map(userRole -> userRoleService.save(userRole));

        return ResponseResult.okResult();
    }


    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        long count = count(queryWrapper);
        if (count>0)return true;
        return false;
    }

    private boolean nickNameExist(String s){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,s);
        long count = count(queryWrapper);
        if (count>0)return true;
        return false;
    }
}

