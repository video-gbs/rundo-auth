package com.runjian.rbac.service;

import com.runjian.rbac.vo.response.GetSectionTreeRsp;

/**
 * @author Miracle
 * @date 2023/5/31 14:41
 */
public interface SectionService {

    /**
     * 获取部门树
     */
    GetSectionTreeRsp getSectionTree();

    /**
     * 新增部门节点
     */
    void addSection(Long pid, String sectionName, String leaderName, String phone);

    /**
     * 删除部门节点
     */
    void deleteSection(Long sectionId);


    /**
     * 父子移动节点
     */
    void fsMove(Long id, Long pid);

    /**
     * 兄弟移动节点
     * @param id
     * @param moveOp
     */
    void btMove(Long id, Integer moveOp);



}
