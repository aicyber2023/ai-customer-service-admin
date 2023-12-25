package com.digitalemployee.business.service.impl;

import java.util.Date;
import java.util.List;

import com.digitalemployee.business.domain.BizDigitalEmployee;
import com.digitalemployee.business.domain.BizQuestionFile;
import com.digitalemployee.business.mapper.BizDigitalEmployeeMapper;
import com.digitalemployee.business.mapper.BizQuestionFileMapper;
import com.digitalemployee.business.service.IBizQuestionFileService;
import com.digitalemployee.common.exception.base.BaseException;
import com.digitalemployee.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;


/**
 * 问答库文档Service业务层处理
 * 
 * @author aicyber
 * @date 2023-11-29
 */
@Service
@RequiredArgsConstructor
public class BizQuestionFileServiceImpl extends ServiceImpl<BizQuestionFileMapper, BizQuestionFile> implements IBizQuestionFileService
{

    private final BizQuestionFileMapper bizQuestionFileMapper;

    private final BizDigitalEmployeeMapper bizDigitalEmployeeMapper;

    /**
     * 查询问答库文档
     * 
     * @param id 问答库文档主键
     * @return 问答库文档
     */
    @Override
    public BizQuestionFile selectBizQuestionFileById(Long id)
    {
        return bizQuestionFileMapper.selectBizQuestionFileById(id);
    }

    /**
     * 查询问答库文档列表
     * 
     * @param bizQuestionFile 问答库文档
     * @return 问答库文档
     */
    @Override
    public List<BizQuestionFile> selectBizQuestionFileList(BizQuestionFile bizQuestionFile)
    {
        return bizQuestionFileMapper.selectBizQuestionFileList(bizQuestionFile);
    }

    /**
     * 新增问答库文档
     * 
     * @param bizQuestionFile 问答库文档
     * @return 结果
     */
    @Override
    public int insertBizQuestionFile(BizQuestionFile bizQuestionFile)
    {
        bizQuestionFile.setCreateTime(DateUtils.getNowDate());
        return bizQuestionFileMapper.insertBizQuestionFile(bizQuestionFile);
    }


    /**
     * 修改问答库文档
     * 
     * @param bizQuestionFile 问答库文档
     * @return 结果
     */
    @Override
    public int updateBizQuestionFile(BizQuestionFile bizQuestionFile)
    {
        bizQuestionFile.setFileName(bizQuestionFile.getFileName()+".xlsx");
        return bizQuestionFileMapper.updateBizQuestionFile(bizQuestionFile);
    }

    /**
     * 批量删除问答库文档
     * 
     * @param ids 需要删除的问答库文档主键
     * @return 结果
     */
    @Override
    public int deleteBizQuestionFileByIds(Long[] ids)
    {
        return bizQuestionFileMapper.deleteBizQuestionFileByIds(ids);
    }

    /**
     * 删除问答库文档信息
     * 
     * @param id 问答库文档主键
     * @return 结果
     */
    @Override
    public int deleteBizQuestionFileById(Long id)
    {
        return bizQuestionFileMapper.deleteBizQuestionFileById(id);
    }
}
