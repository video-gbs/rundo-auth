package com.runjian.rbac.mapper;

import com.runjian.rbac.domain.vo.system.AppMenuApi;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AppMenuApiMapper
 * @Description 应用菜单接口 Mapper 接口
 * @date 2023-02-15 周三 14:47
 */
@Mapper
public interface AppMenuApiMapper {

    List<AppMenuApi> selectByAppType(@Param("appType") Integer appType);
}
