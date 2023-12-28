package com.digitalemployee.business.service.impl;

import java.util.*;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.digitalemployee.business.api.RemoteModelService;
import com.digitalemployee.business.api.domain.BaseResponse;
import com.digitalemployee.business.domain.*;
import com.digitalemployee.business.mapper.BizDigitalEmployeeMapper;
import com.digitalemployee.business.mapper.BizQuestionAnswerMapper;
import com.digitalemployee.business.mapper.BizQuestionFileMapper;
import com.digitalemployee.business.mapper.BizSimilarityQuestionMapper;
import com.digitalemployee.business.service.IBizKnowledgeBaseService;
import com.digitalemployee.business.service.IBizQuestionFileService;
import com.digitalemployee.business.service.IBizSimilarityQuestionService;
import com.digitalemployee.common.exception.base.BaseException;
import com.digitalemployee.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class BizQuestionFileServiceImpl extends ServiceImpl<BizQuestionFileMapper, BizQuestionFile> implements IBizQuestionFileService {

    private final BizQuestionFileMapper bizQuestionFileMapper;

    private final BizDigitalEmployeeMapper bizDigitalEmployeeMapper;

    private final BizQuestionAnswerMapper bizQuestionAnswerMapper;

    private final IBizKnowledgeBaseService bizKnowledgeBaseService;

    private final RemoteModelService remoteModelService;

    private final BizSimilarityQuestionMapper bizSimilarityQuestionMapper;

    /**
     * 查询问答库文档
     *
     * @param id 问答库文档主键
     * @return 问答库文档
     */
    @Override
    public BizQuestionFile selectBizQuestionFileById(Long id) {
        return bizQuestionFileMapper.selectBizQuestionFileById(id);
    }

    /**
     * 查询问答库文档列表
     *
     * @param bizQuestionFile 问答库文档
     * @return 问答库文档
     */
    @Override
    public List<BizQuestionFile> selectBizQuestionFileList(BizQuestionFile bizQuestionFile) {
        return bizQuestionFileMapper.selectBizQuestionFileList(bizQuestionFile);
    }

    /**
     * 新增问答库文档
     *
     * @param bizQuestionFile 问答库文档
     * @return 结果
     */
    @Override
    public int insertBizQuestionFile(BizQuestionFile bizQuestionFile) {
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
    public int updateBizQuestionFile(BizQuestionFile bizQuestionFile) {
        bizQuestionFile.setFileName(bizQuestionFile.getFileName() + ".xlsx");
        return bizQuestionFileMapper.updateBizQuestionFile(bizQuestionFile);
    }

    /**
     * 批量删除问答库文档
     *
     * @param ids 需要删除的问答库文档主键
     * @return 结果
     */
    @Override
    public int deleteBizQuestionFileByIds(Long[] ids) {
        for (Long id : ids) {
            BizQuestionFile bizQuestionFile = bizQuestionFileMapper.selectBizQuestionFileById(id);
            if (bizQuestionFile != null) {
                Long digitalEmployeeId = bizQuestionFile.getDigitalEmployeeId();
                Long knowledgeBaseId = bizKnowledgeBaseService.getKnowledgeBaseIdByDeId(digitalEmployeeId);
                BizKnowledgeBase knowledgeBase = bizKnowledgeBaseService.getById(knowledgeBaseId);
                if (knowledgeBase == null) {
                    throw new RuntimeException("知识库不存在");
                }
                JSONObject paramMap = JSONUtil.createObj();
                paramMap.put("collection", knowledgeBase.getCollectionNameQa());
                log.info("调用删除集合远程接口 START...");
                long start = System.currentTimeMillis();
                BaseResponse response = remoteModelService.dropCollection(paramMap);
                log.info("调用删除集合远程接口 END...共耗时 {} 毫秒", System.currentTimeMillis() - start);
                if (!response.getSuccessful()) {
                    throw new RuntimeException("远程服务调用异常：" + response.getMessage());
                }
                //删除数据和文件
                String questionId = bizQuestionFile.getQuestionId();
                String[] questionIdArray = questionId.split(",");
                List<Long> idList = new ArrayList<>();
                List<Long> similarityIdList = new ArrayList<>();
                for (String s : questionIdArray) {
                    if(!s.equals("")){
                        Long QuestionAnswerId = Long.parseLong(s);
                        BizQuestionAnswer questionAnswer = bizQuestionAnswerMapper.selectBizQuestionAnswerById(QuestionAnswerId);
                        if (questionAnswer != null) {
                            idList.add(questionAnswer.getId());
                        }
                        List<BizSimilarityQuestion> similarityQuestionList = bizSimilarityQuestionMapper.selectBizSimilarityQuestionListById(QuestionAnswerId);
                        similarityQuestionList.forEach(item -> {
                            similarityIdList.add(item.getId());
                        });
                    }
                }
                if(idList.size()!=0){
                    //删除相关问答库
                    Long[] idListArray = idList.toArray(new Long[idList.size()]);
                    bizQuestionAnswerMapper.deleteBizQuestionAnswerByIds(idListArray);
                }
                if(similarityIdList.size()!=0){
                    //删除相关相似问
                    Long[] similarityIdListArray = similarityIdList.toArray(new Long[similarityIdList.size()]);
                    bizSimilarityQuestionMapper.deleteBizSimilarityQuestionByIds(similarityIdListArray);
                }
            }
        }
        //删除问答库文件
        return bizQuestionFileMapper.deleteBizQuestionFileByIds(ids);
    }

    /**
     * 删除问答库文档信息
     *
     * @param id 问答库文档主键
     * @return 结果
     */
    @Override
    public int deleteBizQuestionFileById(Long id) {
        return bizQuestionFileMapper.deleteBizQuestionFileById(id);
    }
}
