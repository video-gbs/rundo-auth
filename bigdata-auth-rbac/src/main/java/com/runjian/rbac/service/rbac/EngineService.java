package com.runjian.rbac.service.rbac;

import com.runjian.rbac.vo.response.GetEngineRsp;

import java.util.List;

/**
 * @author Miracle
 * @date 2024/3/18 17:23
 */
public interface EngineService {

    /**
     * 获取引擎信息
     * @param name 引擎名称
     * @param type 引擎类型
     * @return
     */
    List<GetEngineRsp>  getEnginePage(String name, String type);

    /**
     * 添加引擎
     * @param name
     * @param type
     * @param config
     */
    void addEngine(String name, String type, String config);

    /**
     * 修改引擎
     * @param id
     * @param name
     * @param config
     */
    void updateEngine(Long id, String name, String config);

    /**
     * 删除引擎
     * @param id
     */
    void deleteEngine(Long id);
}
