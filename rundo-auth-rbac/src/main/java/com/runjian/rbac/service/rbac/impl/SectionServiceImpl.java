package com.runjian.rbac.service.rbac.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.dao.SectionMapper;
import com.runjian.rbac.dao.UserMapper;
import com.runjian.rbac.entity.SectionInfo;
import com.runjian.rbac.service.rbac.DataBaseService;
import com.runjian.rbac.service.rbac.SectionService;
import com.runjian.rbac.vo.response.GetSectionTreeRsp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author Miracle
 * @date 2023/5/31 15:23
 */
@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {

    private final SectionMapper sectionMapper;

    private final UserMapper userMapper;

    private final DataBaseService dataBaseService;

    @Override
    public GetSectionTreeRsp getSectionTree() {
        List<GetSectionTreeRsp> sectionInfoList = sectionMapper.selectAllByTree();
        GetSectionTreeRsp rootRsp = GetSectionTreeRsp.getRootSectionTree();
        rootRsp.setChildList(recursionData(sectionInfoList, rootRsp.getLevel()));
        return rootRsp;
    }

    private List<GetSectionTreeRsp> recursionData(List<GetSectionTreeRsp> sectionInfoList, String level){
        List<GetSectionTreeRsp> next = sectionInfoList.stream().filter(sectionInfo -> sectionInfo.getLevel().equals(level)).toList();
        for (GetSectionTreeRsp getSectionTreeRsp : next){
            List<GetSectionTreeRsp> sectionTreeRspList = sectionInfoList.stream()
                    .filter(node -> node.getLevel().startsWith(getSectionTreeRsp.getLevel() + MarkConstant.MARK_SPLIT_RAIL + getSectionTreeRsp.getId())).toList();
            getSectionTreeRsp.setChildList(recursionData(sectionTreeRspList, getSectionTreeRsp.getLevel() + MarkConstant.MARK_SPLIT_RAIL + getSectionTreeRsp.getId()));
        }
        return next.stream().sorted(Comparator.comparing(GetSectionTreeRsp::getSectionSort)).toList();
    }

    @Override
    public void addSection(Long pid, String sectionName, String leaderName, String phone, String description) {
        LocalDateTime nowTime = LocalDateTime.now();
        SectionInfo sectionInfo = new SectionInfo();
        sectionInfo.setSectionName(sectionName);
        sectionInfo.setSectionPid(pid);
        sectionInfo.setLeaderName(leaderName);
        sectionInfo.setPhone(phone);
        sectionInfo.setSectionSort(System.currentTimeMillis());
        sectionInfo.setCreateTime(nowTime);
        sectionInfo.setUpdateTime(nowTime);
        sectionInfo.setDescription(description);
        if (pid.equals(0L)){
            sectionInfo.setLevel("0");
        }else {
            SectionInfo fatherSectionInfo = dataBaseService.getSectionInfo(pid);
            sectionInfo.setLevel(fatherSectionInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + fatherSectionInfo.getId());
        }
        sectionMapper.save(sectionInfo);
    }

    @Override
    public void updateSection(Long id, String sectionName, String leaderName, String phone, String description) {
        SectionInfo sectionInfo = dataBaseService.getSectionInfo(id);
        sectionInfo.setSectionName(sectionName);
        sectionInfo.setLeaderName(leaderName);
        sectionInfo.setPhone(phone);
        sectionInfo.setDescription(description);
        sectionMapper.updateAll(List.of(sectionInfo));
    }

    @Override
    public void deleteSection(Long sectionId) {
        SectionInfo sectionInfo = dataBaseService.getSectionInfo(sectionId);
        Integer userCount = userMapper.selectCountBySectionId(sectionId);
        if (userCount > 0){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, String.format("部门 %s 存在绑定用户，请先解绑后删除", sectionInfo.getSectionName()));
        }
        Integer sectionCount = sectionMapper.selectChildCountByPid(sectionInfo.getSectionPid());
        if (sectionCount > 0){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, String.format("部门 %s 存在下级部门，请先删除下级部门", sectionInfo.getSectionName()));
        }
        sectionMapper.deleteById(sectionId);
    }

    @Override
    public void fsMove(Long id, Long pid) {
        SectionInfo sectionInfo = dataBaseService.getSectionInfo(id);
        SectionInfo pSectionInfo;
        String level;
        if (pid.equals(0L)){
            pSectionInfo = new SectionInfo();
            pSectionInfo.setId(0L);
            pSectionInfo.setLevel("0");
            level = pSectionInfo.getLevel();
        }else {
            pSectionInfo = dataBaseService.getSectionInfo(pid);
            level = pSectionInfo.getLevel()+ MarkConstant.MARK_SPLIT_RAIL + pSectionInfo.getId();
        }
        if (pSectionInfo.getSectionPid().equals(sectionInfo.getSectionPid())){
            return;
        }
        if (pSectionInfo.getLevel().startsWith(sectionInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + sectionInfo.getId())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "不可将父部门移动到子部门");
        }
        LocalDateTime nowTime = LocalDateTime.now();
        String oldLevel = sectionInfo.getLevel();
        sectionInfo.setLevel(level);
        sectionInfo.setSectionPid(pid);
        sectionInfo.setSectionSort(nowTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        sectionInfo.setUpdateTime(nowTime);
        List<SectionInfo> childList = sectionMapper.selectAllLikeByLevel(oldLevel + MarkConstant.MARK_SPLIT_RAIL + sectionInfo.getId());
        for (SectionInfo child : childList){
            child.setUpdateTime(nowTime);
            child.setLevel(sectionInfo.getLevel() + child.getLevel().substring(0, oldLevel.length()));
        }
        childList.add(sectionInfo);
        sectionMapper.updateAll(childList);
    }

    @Override
    public void btMove(Long id, Integer moveOp) {
        SectionInfo sectionInfo = dataBaseService.getSectionInfo(id);
        List<SectionInfo> brotherList = sectionMapper.selectChildByPid(sectionInfo.getSectionPid());
        if (brotherList.size() == 1){
            return;
        }
        brotherList.sort(Comparator.comparing(SectionInfo::getSectionSort));
        for (int i = 0; i < brotherList.size(); i++){
            SectionInfo pointData = brotherList.get(i);
            if (pointData.getId().equals(id)){
                SectionInfo brother;
                if (moveOp.equals(1)){
                    if (i == 0){
                        return;
                    }
                    brother = brotherList.get(i - 1);
                }else if (moveOp.equals(0)){
                    if (i == brotherList.size() - 1){
                        return;
                    }
                    brother = brotherList.get(i + 1);
                } else {
                    throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "未知的移动操作");
                }
                LocalDateTime nowTime = LocalDateTime.now();
                Long oldSort = brother.getSectionSort();
                brother.setSectionSort(pointData.getSectionSort());
                pointData.setSectionSort(oldSort);
                brother.setUpdateTime(nowTime);
                pointData.setUpdateTime(nowTime);
                sectionMapper.updateAll(Arrays.asList(brother, pointData));
            }
        }
    }

}
