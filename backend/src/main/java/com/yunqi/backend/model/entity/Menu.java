package com.yunqi.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yunqi.backend.common.base.BaseEntity;
import lombok.Data;


/**
 * 菜单权限表 sys_menu
 *
 * @author ruoyi
 */
@Data
public class Menu extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 路由地址
     */
    private String routerPath;

    /**
     * 菜单全路径，用于在数据库中标识上下级
     */
    private String pathCode;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 路由参数
     */
    private String queryParams;

    /**
     * 类型（M目录 C菜单 F按钮）
     */
    private String menuType;

    /**
     * 菜单状态（0正常 1停用）
     */
    private String status;

    /**
     * 权限字符串
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

}
