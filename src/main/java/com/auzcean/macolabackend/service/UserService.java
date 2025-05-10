package com.auzcean.macolabackend.service;

import com.auzcean.macolabackend.model.dto.user.UserQueryRequest;
import com.auzcean.macolabackend.model.entity.User;
import com.auzcean.macolabackend.model.vo.LoginUserVo;
import com.auzcean.macolabackend.model.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author xchuckie
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-05-10 11:07:30
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 密码盐值
     *
     * @param password  混淆密码
     * @return  盐值密码
     */
    String getEncryptPassword(String password);

    /**
     * 用户登录
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param request       请求服务
     * @return 脱敏用户
     */
    LoginUserVo userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return 脱敏用户
     */
    LoginUserVo getLoginUserVO(User user);

    /**
     * 获取当前登录用户
     *
     * @param request	服务端请求服务
     * @return	返回用户信息
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 对用户信息进行脱敏
     *
     * @param user  用户
     * @return  脱敏后的用户信息
     */
    UserVo getUserVo(User user);

    /**
     * 获得脱敏后的用户列表
     *
     * @param userList  用户列表
     * @return 脱敏后的用户列表
     */
    List<UserVo> getUserVoList(List<User> userList);

    /**
    * 用户注销
    *
    * @param request    服务请求服务
    * @return	返回注销成功信息
    */
    boolean userLogout(HttpServletRequest request);

    /**
     * 分页查询请求封装成
     *
     * @param userQueryRequest 分页查询请求
     * @return 查询结果
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);


}
