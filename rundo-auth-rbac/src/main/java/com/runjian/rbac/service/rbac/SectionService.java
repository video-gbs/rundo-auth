package com.runjian.rbac.service.rbac;

import com.runjian.rbac.vo.response.GetSectionTreeRsp;

/**
 * @author Miracle
 * @date 2023/5/31 14:41
 */
public interface SectionService {

    /**
     * 获取部门树
     * @return GetSectionTreeRsp
     */
    GetSectionTreeRsp getSectionTree();

    /**
     * 新增部门节点
     * @param pid 父id
     * @param sectionName 部门名称
     * @param leaderName 领导名称
     * @param phone 电话号码
     * @param description 描述
     */
    void addSection(Long pid, String sectionName, String leaderName, String phone, String description);

    /**
     * 修改部门
     * @param id 部门id
     * @param sectionName 部门名称
     * @param leaderName 领导名称
     * @param phone 电话号码
     * @param description 描述
     */
    void updateSection(Long id, String sectionName, String leaderName, String phone, String description);

    /**
     * 删除部门节点
     * @param sectionId 部门id
     */
    void deleteSection(Long sectionId);


    /**
     * 父子移动节点
     * @param id 部门id
     * @param pid 父id
     */
    void fsMove(Long id, Long pid);

    /**
     * 兄弟移动节点
     * @param id 部门id
     * @param moveOp 1上移 0下移
     */
    void btMove(Long id, Integer moveOp);



}
