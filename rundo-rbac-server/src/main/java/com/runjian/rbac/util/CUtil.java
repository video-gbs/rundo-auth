package com.runjian.rbac.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class CUtil {

    public List<Long> removeRepLong(List<Long> list){
        final boolean sta = null != list && list.size() > 0;
        List doubleList= new ArrayList();
        if (sta) {
            HashSet<Long> set = new HashSet<>(list);
            set.addAll(list);
            doubleList.addAll(set);
        }
        return doubleList;
    }

    public List<String> removeRepString(List<String> list){
        final boolean sta = null != list && list.size() > 0;
        List doubleList= new ArrayList();
        if (sta) {
            HashSet<String> set = new HashSet<>(list);
            set.addAll(list);
            doubleList.addAll(set);
        }
        return doubleList;
    }
}
