package com.runjian.rbac.service.rbac;

import com.runjian.rbac.vo.response.GetDatasourceRsp;
import com.runjian.rbac.vo.response.GetUserDatasourceRsp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Miracle
 * @date 2024/3/18 18:09
 */
public interface DatasourceService {

    /**
     * 分页获取数据源
     * @param pageNum
     * @param pageSize
     * @param name
     * @param type
     * @param module
     * @return
     */
    List<GetDatasourceRsp> getDatasourcePage(int pageNum, int pageSize, String name, String type, String module);

    /**
     * 添加数据源
     * @param type
     * @param module
     * @param name
     * @param description
     * @param config
     */
    void addDatasource(String type, String module, String name, String description, String config);

    /**
     * 修改数据源
     * @param datasourceId
     * @param nam
     * @param description
     * @param config
     */
    void updateDatasource(Long datasourceId, String name, String description, String config);

    /**
     * 删除数据源
     * @param datasourceId
     */
    void deleteDatasource(Long datasourceId);

    /**
     * 获取授权数据源信息
     * @param pageNum
     * @param pageSize
     * @param datasourceId
     * @param name
     * @param mainType
     * @return
     */
    List<GetUserDatasourceRsp> getDatasourceAuthPage(int pageNum, int pageSize, Long datasourceId, String name, Integer mainType);

    /**
     * 添加授权数据源
     * @param readWriteAuth
     * @param userIds
     * @param organizeIds
     * @param userValidTime
     * @param organizeValidTime
     * @param authReason
     */
    void addDatasourceAuth(Integer readWriteAuth, List<Long> userIds, List<Long> organizeIds, LocalDateTime userValidTime, LocalDateTime organizeValidTime, String authReason);

    /**
     * 修改授权数据源的过期时间
     * @param datasourceAuthId
     * @param validTime
     */
    void updateDatasourceAuthValidTime(Long datasourceAuthId, LocalDateTime validTime);

    /**
     * 删除授权数据源
     * @param datasourceAuthId
     */
    void deleteDatasourceAuth(Long datasourceAuthId);
}
