package com.mengtu.kaichi.serviceimpl.label;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengtu.kaichi.label.mapper.CaseMapper;
import com.mengtu.kaichi.label.mapper.LabelMapper;
import com.mengtu.kaichi.label.pojo.Case;
import com.mengtu.kaichi.label.vo.CaseVo;
import com.mengtu.kaichi.serviceimpl.label.service.CaseService;
import com.mengtu.util.storage.FileUtil;
import com.mengtu.util.storage.ObsUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Service
public class CaseServiceImpl implements CaseService {

    @Resource
    private CaseMapper caseMapper;

    @Resource
    private LabelMapper labelMapper;

    @Resource
    ObsUtil obsUtil;

    private static final String CASE_MP4 = "case/mp4/";
    private static final String CASE_MP4_PIC = "case/mp4/pic/";

    @Override
    public List<Case> selectAll(String labelId) {
        List<Case> cases = caseMapper.selectList(new QueryWrapper<Case>().eq("label_id",labelId));
        for (Case c:cases) {
            c.setCaseMp4(obsUtil.getSignatureDownloadUrl(
                    CASE_MP4, c.getCaseUrl()+"v"));
            c.setCasePic(obsUtil.getSignatureDownloadUrl(
                    CASE_MP4_PIC, c.getCaseUrl()+"p"));
        }
        return cases;
    }

    @Override
    public CaseVo selectByCaseId(String caseId) {
        CaseVo caseVo = new CaseVo();
        Case c = caseMapper.selectById(caseId);
        c.setCaseMp4(obsUtil.getSignatureDownloadUrl(
                CASE_MP4, c.getCaseUrl()+"v"));
        c.setCasePic(obsUtil.getSignatureDownloadUrl(
                CASE_MP4_PIC, c.getCaseUrl()+"p"));
        caseVo.setACase(c);
        caseVo.setLabel(labelMapper.selectById(c.getLabelId()));
        return caseVo;
    }

    @Override
    public int insert(Case c) {
        File mp4 = FileUtil.multipartFileToFile(c.getMp4());
        File pic = FileUtil.multipartFileToFile(c.getPic());
        String originalFileName = mp4.getName();
        obsUtil.uploadFile(mp4, CASE_MP4, c.getCaseUrl()+"v" + originalFileName.substring(originalFileName.lastIndexOf(".")));
        originalFileName = pic.getName();
        obsUtil.uploadFile(pic, CASE_MP4_PIC, c.getCaseUrl()+"p" + originalFileName.substring(originalFileName.lastIndexOf(".")));
        return caseMapper.insert(c);
    }

    @Override
    public int update(Case c) {
        Case c1 = caseMapper.selectById(c.getCaseId());
        obsUtil.deleteFile(CASE_MP4, c1.getCaseUrl());
        obsUtil.deleteFile(CASE_MP4_PIC, c1.getCaseUrl());

        File mp4 = FileUtil.multipartFileToFile(c.getMp4());
        File pic = FileUtil.multipartFileToFile(c.getPic());
        String originalFileName = mp4.getName();
        obsUtil.uploadFile(mp4, CASE_MP4, c1.getCaseUrl()+"v" + originalFileName.substring(originalFileName.lastIndexOf(".")));
        originalFileName = pic.getName();
        obsUtil.uploadFile(pic, CASE_MP4_PIC, c1.getCaseUrl()+"p" + originalFileName.substring(originalFileName.lastIndexOf(".")));
        return caseMapper.update(c,new QueryWrapper<Case>().eq("case_id",c.getCaseId()));
    }

    @Override
    public int delete(String caseId) {
        //        obsUtil.deleteFile(CASE_MP4, c.getCaseUrl());
//        obsUtil.deleteFile(CASE_MP4_PIC, c.getCaseUrl());
        return caseMapper.delete(new QueryWrapper<Case>().eq("case_id",caseId));
    }
}
