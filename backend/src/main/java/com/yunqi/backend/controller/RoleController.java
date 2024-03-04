package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.PageResult;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.dto.RoleDTO;
import com.yunqi.backend.model.dto.UserDTO;
import com.yunqi.backend.model.dto.UserRoleDTO;
import com.yunqi.backend.model.entity.Role;
import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.service.RoleService;
import com.yunqi.backend.service.UserRoleService;
import com.yunqi.backend.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 角色控制器
 * @author liyunqi
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Resource
    RoleService roleService;

    @Resource
    UserService userService;

    @Resource
    UserRoleService userRoleService;

    @GetMapping("/list")
    public Result<PageResult> getRolePage(RoleDTO roleDTO) {
        Page<Role> page = roleService.getRolePage(roleDTO);
        return Result.success(PageUtils.convertPageResult(page));
    }

    @GetMapping("/{roleId}")
    public Result<Role> getRoleById(@PathVariable Long roleId) {
        return Result.success(roleService.getById(roleId));
    }

    /**
     * 保存角色
     * @param roleDTO
     * @return
     */
    @PostMapping
    public Result saveRole(@Validated @RequestBody RoleDTO roleDTO) {
        roleService.saveRole(roleDTO);
        return Result.success();
    }

    @PutMapping
    public Result updateRole(@Validated @RequestBody RoleDTO roleDTO) {
        roleService.updateRole(roleDTO);
        return Result.success();
    }

    /**
     * 根据id更新状态
     * @param roleDTO
     * @return
     */
    @PutMapping("/changeStatus")
    public Result changeStatus(@RequestBody RoleDTO roleDTO) {
        roleService.changeStatus(roleDTO);
        return Result.success();
    }

    @DeleteMapping("/{roleIds}")
    public Result deleteRoleByIds(@PathVariable List<Long> roleIds) {
        roleService.deleteRoleByIds(roleIds);
        return Result.success();
    }

    /**
     * 查询已分配用户角色列表
     */
    @GetMapping("/authUser/allocatedList")
    public Result allocatedList(UserDTO userDTO, Long roleId) {
        Page<User> page = userService.selectAllocatedList(userDTO, roleId);
        return Result.success(PageUtils.convertPageResult(page));
    }

    /**
     * 查询未分配用户角色列表
     */
    @GetMapping("/authUser/unallocatedList")
    public Result unallocatedList(UserDTO userDTO) {
        Page<User> page = userService.selectUnallocatedList(userDTO);
        return Result.success(PageUtils.convertPageResult(page));
    }

    /**
     * 取消授权用户
     */
    @PutMapping("/authUser/cancel")
    public Result cancelAuthUser(@RequestBody UserRoleDTO userRoleDTO) {
        userRoleService.deleteAuthUser(userRoleDTO);
        return Result.success();
    }

    /**
     * 批量取消授权用户
     */
    @PutMapping("/authUser/cancelAll")
    public Result cancelAuthUserAll(Long roleId, Long[] userIds) {
        userRoleService.deleteAuthUsers(roleId, Arrays.asList(userIds));
        return Result.success();
    }

    /**
     * 批量选择用户授权
     */
    @PutMapping("/authUser/selectAll")
    public Result selectAuthUserAll(Long roleId, Long[] userIds) {
        userRoleService.insertUserRole(roleId, Arrays.asList(userIds));
        return Result.success();
    }

}
