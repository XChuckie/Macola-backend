package com.auzcean.macolabackend.aop;

import com.auzcean.macolabackend.annotation.AuthCheck;
import com.auzcean.macolabackend.exception.BusinessException;
import com.auzcean.macolabackend.exception.ErrorCode;
import com.auzcean.macolabackend.model.entity.User;
import com.auzcean.macolabackend.model.enums.UserRoleEnum;
import com.auzcean.macolabackend.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Aspect  // 切面注解
@Component  // Spring中bean实例注解，Spring会自动加载该bean对象
public class AuthInterceptor {
    
    @Resource
    private UserService userService;

    /**
     * 执行拦截
     *
     * @param joinPoint 连接点，起到连接业务中插入注解的位置
     * @param authCheck 权限校验注解
     * @return 通过执行
     */
    @Around(value = "@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        // 1. 获取当前登录用户 和 用户权限
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        User currentUser = userService.getLoginUser(request);
        // 指定用户权限
        String mustRole = authCheck.mustRole();
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);

        // 2.权限判断
        // 如果不需要权限，则放行
        if (mustRoleEnum == null) {
            return joinPoint.proceed();  // 对于没有指定角色的接口可直接访问，即：无权限登录接口
        }
        // 接下来：必须有权限才能够通过
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(currentUser.getUserRole());
        // 没有权限，则拒绝 -> 必须制定用户或者管理员
        if (userRoleEnum == null) throw new BusinessException(ErrorCode.NO_AUTH_ERROR);

        // 要求必须是管理员才能够访问，而用户没有管理员权限则会被拒绝
        if (UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}