package com.runjian.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.runjian.rbac.domain.entity.ConfigInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统全局参数配置 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface ConfigInfoMapper extends BaseMapper<ConfigInfo> {

}
