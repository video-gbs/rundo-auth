package com.runjian.rbac.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.stereotype.Component;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SnowflakeUtil
 * @Description 雪花ID生成
 * @date 2023-01-10 周二 11:30
 */
@Component
public class RundoIdUtil {
    Snowflake snowflake = IdUtil.getSnowflake(1, 1);

    public Long nextId() {
        return snowflake.nextId();
    }

    public String nextIdStr() {
        return snowflake.nextIdStr();
    }
}
