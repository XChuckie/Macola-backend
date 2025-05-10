package com.auzcean.macolabackend.controller;

import com.auzcean.macolabackend.annotation.AuthCheck;
import com.auzcean.macolabackend.common.BaseResponse;
import com.auzcean.macolabackend.common.DeleteRequest;
import com.auzcean.macolabackend.common.ResultUtils;
import com.auzcean.macolabackend.constant.UserConstant;
import com.auzcean.macolabackend.exception.BusinessException;
import com.auzcean.macolabackend.exception.ErrorCode;
import com.auzcean.macolabackend.exception.ThrowUtils;
import com.auzcean.macolabackend.model.dto.user.*;
import com.auzcean.macolabackend.model.entity.User;
import com.auzcean.macolabackend.model.enums.UserRoleEnum;
import com.auzcean.macolabackend.model.vo.LoginUserVo;
import com.auzcean.macolabackend.model.vo.UserVo;
import com.auzcean.macolabackend.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 请求参数
     * @return  返回注册成功信息
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     * @param userLoginRequest 请求参数
     * @param request 请求服务
     * @return 返回登录成功信息
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVo> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVo loginUserVo = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVo);
    }

    /**
     * 得到用户信息
     *
     * @param request 请求服务
     * @return 脱敏
     */
    @PostMapping("/get/login")
    public BaseResponse<UserVo> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getUserVo(loginUser));
    }

    /**
     * 用户退出登录
     *
     * @param request 请求服务
     * @return 成功退出
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        boolean result = userService.userLogout(request);

        return ResultUtils.success(result);
    }

    /**
     * 增加用户（仅管理员）
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);

        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 1. 设置默认密码
        String DEFAULT_PASSWORD = "12345678";
        String encryptPassword = userService.getEncryptPassword(DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        // 2. 更新数据
        try {
            boolean result = userService.save(user);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户账号已经存在");
        }
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户（仅管理员）
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() < 0, ErrorCode.PARAMS_ERROR);
        boolean result = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 更新用户（仅管理员）
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        ThrowUtils.throwIf(userUpdateRequest == null || userUpdateRequest.getId() < 0, ErrorCode.PARAMS_ERROR);

        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "更新参数失败");
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     * @return 返回不脱敏数据
     */
    @PostMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     * @return 脱敏后的数据
     */
    @PostMapping("/get/vo")
    public BaseResponse<UserVo> getUserVoById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        UserVo userVo = userService.getUserVo(user);
        return ResultUtils.success(userVo);
    }

    /**
     * 分页查询（管理员）
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVo>> listUserVoByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);

        int current = userQueryRequest.getCurrent();
        int pageSize = userQueryRequest.getPageSize();

        // 1. 根据参数调用封装的QueryWrapper对象
        Page<User> userPage = userService.page(new Page<>(current, pageSize), userService.getQueryWrapper(userQueryRequest));

        // 2. 对查询结果进行脱敏
        List<UserVo> userVoList = userService.getUserVoList(userPage.getRecords());
        Page<UserVo> userVoPage = new Page<>(current, pageSize, userPage.getTotal());
        userVoPage.setRecords(userVoList);

        return ResultUtils.success(userVoPage);
    }

}
