package com.runjian.rbac.service;

import com.github.pagehelper.PageInfo;
import com.runjian.rbac.vo.response.GetFuncPageRsp;
import com.runjian.rbac.vo.response.GetFuncResourceRsp;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/6/6 9:41
 */
public interface FuncService {

    /**
     * 查询功能列表
     * @param page 页数
     * @param num 数量
     * @param menuId 菜单id
     * @param serviceName 服务名称
     * @param funcName 功能名称
     * @param isInclude 是否包含下级节点
     * @return
     */
    PageInfo<GetFuncPageRsp> getFuncPage(int page, int num, Long menuId, String serviceName, String funcName, Boolean isInclude);

    /**
     * 添加功能
     * @param menuId 菜单id
     * @param serviceName 服务名称
     * @param funcName 功能名称
     * @param path 访问地址
     * @param method 方法
     * @param disabled 是否禁用
     */
    void addFunc(Long menuId, String serviceName, String funcName, String path, Integer method, Integer disabled);


    /**
     * 禁用状态改变
     * @param id 功能id
     * @param disabled 是否禁用
     */
    void updateDisabled(Long id, Integer disabled);

    /**
     * 修改功能
     * @param id 功能id
     * @param serviceName 服务名称
     * @param funcName 功能名称
     * @param path 访问地址
     * @param method 方法
     * @param disabled 是否禁用
     */
    void updateFunc(Long id, Long menuId, String serviceName, String funcName, String path, Integer method, Integer disabled);

    /**
     * 删除功能
     * @param id 功能id
     */
    void deleteFunc(Long id);

    /**
     * 获取功能关系资源
     * @param funcId 功能id
     */
    List<GetFuncResourceRsp> getFuncResource(Long funcId);

    /**
     * 关联资源
     * @param funcId 功能id
     * @param resourceKey 资源key
     * @param validated 是否校验
     * @param validateParam 校验参数
     * @param disabled 是否禁用
     */
    void associationResource(Long funcId, String resourceKey, Integer validated, String validateParam, Integer disabled);

    /**
     * 修改校验状态
     * @param funcResourceId 功能关联资源id
     * @param validated 是否校验数据
     */
    void updateFuncResourceValidated(Long funcResourceId, Integer validated);

    /**
     * 修改禁用状态
     * @param funcResourceId 功能关联资源id
     * @param disabled 是否禁用
     */
    void updateFuncResourceDisabled(Long funcResourceId, Integer disabled);

    /**
     * 修改关联资源
     * @param funcResourceId 功能资源关系id
     * @param resourceKey 资源key
     * @param validated 是否校验
     * @param validateParam 校验参数
     * @param disabled 是否禁用
     */
    void updateFuncResource(Long funcResourceId, String resourceKey, Integer validated, String validateParam, Integer disabled);

    /**
     * 删除关联资源
     * @param funcResourceId 功能资源关系id
     */
    void deleteFuncResource(Long funcResourceId);


}
