package com.runjian.rbac.service;

import com.runjian.rbac.entity.SectionInfo;
import com.runjian.rbac.entity.UserInfo;

/**
 * @author Miracle
 * @date 2023/6/1 9:15
 */
public interface DataBaseService {

    /**
     * 查询部门
     */
    SectionInfo getSectionInfo(Long id);

    /**
     * 查询用户
     * @param id
     * @return
     */
    UserInfo getUserInfo(Long id);
}
