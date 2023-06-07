package com.runjian.rbac.service.rbac.impl;

import com.runjian.rbac.service.rbac.SectionService;
import com.runjian.rbac.vo.response.GetSectionTreeRsp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Miracle
 * @date 2023/5/31 16:07
 */
@SpringBootTest
class SectionServiceImplTest {

    @Autowired
    private SectionService sectionService;
    @Test
    void getSectionTree() {
        GetSectionTreeRsp sectionTree = sectionService.getSectionTree();
        System.out.println(sectionTree);
    }

    @Test
    void addSection() {
    }

    @Test
    void deleteSection() {
    }

    @Test
    void fsMove() {
    }

    @Test
    void btMove() {
    }
}