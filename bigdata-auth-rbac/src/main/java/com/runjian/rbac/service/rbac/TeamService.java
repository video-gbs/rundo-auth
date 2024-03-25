package com.runjian.rbac.service.rbac;

import com.runjian.rbac.vo.response.GetTeamRsp;

import java.util.List;

/**
 * @author Miracle
 * @date 2024/3/21 14:57
 */
public interface TeamService {

    /**
     * 分页获取团队信息
     * @param pageNum
     * @param pageSize
     * @param name
     * @param disabled
     * @return
     */
    List<GetTeamRsp> getTeamPage(int pageNum, int pageSize, String name, String disabled);

    /**
     * 添加团队信息
     * @param name 团队名称
     * @param description 描述
     */
    void addTeam(String name, String description);

    /**
     * 修改团队信息
     * @param teamId 团队id
     * @param name 团队名称
     * @param description 描述
     */
    void updateTeam(Long teamId, String name, String description);

    /**
     * 删除团队
     * @param teamId 团队id
     */
    void deleteTeam(Long teamId);

    /**
     * 修改团队禁用状态
     * @param teamId 团队id
     * @param disabled 禁用状态
     */
    void updateDisabledTeam(Long teamId, Integer disabled);

}
