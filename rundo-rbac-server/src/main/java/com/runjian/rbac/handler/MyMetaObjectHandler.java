package com.runjian.rbac.handler;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.runjian.rbac.domain.entity.UserInfo;
import com.runjian.rbac.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName MyMetaObjectHandler
 * @Description 自动填充
 * @date 2023-01-28 周六 18:13
 */

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入操作，自动填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // todo 修改createdBy人

        log.info("MybatisPlus 添加时自动参数填充");
        this.setFieldValByName("deleteFlag", 0, metaObject);
        this.setFieldValByName("createdTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("createdBy", 1, metaObject);
        this.setFieldValByName("updatedTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updatedBy", 1, metaObject);

    }

    /**
     * 更新操作，自动填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("MybatisPlus 更新时自动参数填充");
        this.setFieldValByName("updatedTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updatedBy", 1, metaObject);
    }
}
