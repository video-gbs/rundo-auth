package com.runjian.rbac.vo.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门树返回体
 * @author Miracle
 * @date 2023/5/31 14:58
 */
@Data
public class GetSectionTreeRsp {

    private Long id;

    /**
     * 部门父id
     */
    private Long sectionPid;

    /**
     * 部门名称
     */
    private String sectionName;

    /**
     * 部门排序
     */
    private Long sectionSort;

    /**
     * 负责人名称
     */
    private String leaderName;

    /**
     * 电话
     */
    private String phone;

    /**
     * 层级
     */
    private String level;

    /**
     * 描述
     */
    private String description;

    /**
     * 子节点
     */
    private List<GetSectionTreeRsp> childList = new ArrayList<>();

    public static GetSectionTreeRsp getRootSectionTree(){
        GetSectionTreeRsp getSectionTreeRsp = new GetSectionTreeRsp();
        getSectionTreeRsp.setId(0L);
        getSectionTreeRsp.setSectionPid(null);
        getSectionTreeRsp.setSectionName("根节点");
        getSectionTreeRsp.setSectionSort(0L);
        getSectionTreeRsp.setLeaderName("ADMIN");
        getSectionTreeRsp.setPhone(null);
        getSectionTreeRsp.setLevel("0");
        return getSectionTreeRsp;
    }

}
