package com.runjian.rbac.service.rbac;

import com.github.pagehelper.PageInfo;
import com.runjian.rbac.vo.response.GetDictGroupRsp;
import com.runjian.rbac.vo.response.GetDictPageRsp;

import java.util.List;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/6/5 9:46
 */
public interface DictService {

    /**
     * 分页查询字典
     * @param groupName 分组名称
     * @param itemName 字典名称
     * @return GetDictPageRsp
     */
    PageInfo<GetDictPageRsp> getDictPageRsp(int page, int num, String groupName, String itemName);

    /**
     * 获取字典
     * @param groupName 分组名称
     * @return GetDictGroupRsp
     */
    List<GetDictGroupRsp> getDictByGroupName(String groupName);

    /**
     * 增加字典
     * @param groupName 分组名称
     * @param groupCode 分组编码
     * @param itemName 字典名称
     * @param itemValue 字典值
     * @param description 描述
     */
    void addDict(String groupName, String groupCode, String itemName, String itemValue, String description);

    /**
     * 删除字典
     * @param dictIds 字典ID数组
     */
    void batchDelete(Set<Long> dictIds);

    /**
     * 修改字典
     * @param dictId 字典ID
     * @param groupName 分组名称
     * @param groupCode 分组编码
     * @param itemName 字典名称
     * @param itemValue 字典值
     * @param description 描述
     */

    void updateDict(Long dictId, String groupName, String groupCode, String itemName, String itemValue, String description);

}
