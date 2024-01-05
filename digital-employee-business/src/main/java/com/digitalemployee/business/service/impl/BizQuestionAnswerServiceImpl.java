package com.digitalemployee.business.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.business.api.RemoteModelService;
import com.digitalemployee.business.api.domain.AppendQaResponse;
import com.digitalemployee.business.domain.*;
import com.digitalemployee.business.mapper.BizDigitalEmployeeMapper;
import com.digitalemployee.business.mapper.BizQuestionAnswerMapper;
import com.digitalemployee.business.mapper.BizQuestionFileMapper;
import com.digitalemployee.business.mapper.BizSimilarityQuestionMapper;
import com.digitalemployee.business.modules.config.ChatResourcesConfig;
import com.digitalemployee.business.service.IBizKnowledgeBaseService;
import com.digitalemployee.business.service.IBizQuestionAnswerService;
import com.digitalemployee.common.exception.base.BaseException;
import com.digitalemployee.common.utils.DateUtils;
import com.digitalemployee.common.utils.StringUtils;
import com.digitalemployee.common.utils.file.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 问答库Service业务层处理
 *
 * @author aicyber
 * @date 2023-11-27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BizQuestionAnswerServiceImpl extends ServiceImpl<BizQuestionAnswerMapper, BizQuestionAnswer> implements IBizQuestionAnswerService {

    private final BizQuestionAnswerMapper bizQuestionAnswerMapper;

    private final BizSimilarityQuestionMapper bizSimilarityQuestionMapper;

    private final BizDigitalEmployeeMapper bizDigitalEmployeeMapper;

    private final BizQuestionFileMapper bizQuestionFileMapper;

    private final ChatResourcesConfig chatResourcesConfig;

    private final IBizKnowledgeBaseService bizKnowledgeBaseService;

    private final RemoteModelService remoteModelService;


    /**
     * 查询问答库
     *
     * @param id 问答库主键
     * @return 问答库
     */
    @Override
    public BizQuestionAnswer selectBizQuestionAnswerById(Long id) {
        BizQuestionAnswer bizQuestionAnswer = bizQuestionAnswerMapper.selectBizQuestionAnswerById(id);
        if (bizQuestionAnswer == null) {
            throw new BaseException("该数据不存在");
        }
        return bizQuestionAnswer;
    }


    /**
     * 查询问答库列表
     *
     * @param bizQuestionAnswer 问答库
     * @return 问答库
     */
    @Override
    public List<BizQuestionAnswer> selectBizQuestionAnswerList(BizQuestionAnswer bizQuestionAnswer, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        List<BizQuestionAnswer> list = new ArrayList<>();
        List<BizQuestionAnswer> bizQuestionAnswerList = bizQuestionAnswerMapper.selectBizQuestionAnswerList(bizQuestionAnswer, startTime, endTime);
        if (!bizQuestionAnswerList.isEmpty()) {
            for (BizQuestionAnswer questionAnswer : bizQuestionAnswerList) {
                list.add(questionAnswer);
            }
        }
        return list;
    }

    @Override
    public List<BizQuestionAnswer> selectBizQuestionAnswerListBydeId(Long digitalEmployeeId) {
        return bizQuestionAnswerMapper.selectBizQuestionAnswerListBydeId(digitalEmployeeId);
    }

    /**
     * 新增问答库
     *
     * @param bizQuestionAnswer 问答库
     * @return 结果
     */
    @Override
    @Transactional
    public int insertBizQuestionAnswer(BizQuestionAnswer bizQuestionAnswer) {
        bizQuestionAnswer.setCreateTime(DateUtils.getNowDate());
        bizQuestionAnswer.setCreateType(1);
        bizQuestionAnswer.setStatus(1);
        BizQuestionAnswer questionAnswer = bizQuestionAnswerMapper.selectOneBizQuestionAnswer(bizQuestionAnswer);
        if (questionAnswer != null) {
            throw new BaseException("该问答数据已存在");
        }
        int i = bizQuestionAnswerMapper.insertBizQuestionAnswer(bizQuestionAnswer);
        int j = 0;
        if (i > 0) {
            List<BizSimilarityQuestion> similarityList = new ArrayList<>();
            if (!similarityList.isEmpty()) {
                j = bizSimilarityQuestionMapper.batchSimilarityQuestion(similarityList);
            } else {
                j = 1;
            }
        }
        if (i > 0 && j > 0) {
            return 1;
        }
        return -1;
    }

    /**
     * 修改问答库
     *
     * @param bizQuestionAnswer 问答库
     * @return 结果
     */
    @Override
    @Transactional
    public int updateBizQuestionAnswer(BizQuestionAnswer bizQuestionAnswer) {
        bizQuestionAnswer.setUpdateTime(DateUtils.getNowDate());
        BizQuestionAnswer questionAnswer = bizQuestionAnswerMapper.selectBizQuestionAnswer(bizQuestionAnswer);
        if (questionAnswer != null) {
            throw new BaseException("该问答数据已存在,请填写新的问答数据");
        }
        //根据数字员工id找到collectionid,
        BizQuestionAnswer questionAnswerDB = bizQuestionAnswerMapper.selectBizQuestionAnswerById(bizQuestionAnswer.getId());
        String collectionId = questionAnswerDB.getCollectionId();
        if (collectionId != null) {
            // 调用远程删除接口删除要修改的数据
            Long knowledgeBaseId = bizKnowledgeBaseService.getKnowledgeBaseIdByDeId(bizQuestionAnswer.getDigitalEmployeeId());
            BizKnowledgeBase knowledgeBase = bizKnowledgeBaseService.getById(knowledgeBaseId);
            if (knowledgeBase == null) {
                throw new RuntimeException("知识库不存在");
            }
            List<String> collectionList = new ArrayList<>();
            collectionList.add(collectionId);
            String collectionNameQa = knowledgeBase.getCollectionNameQa();
            JSONObject paramMap = new JSONObject();
            paramMap.put("collection", collectionNameQa);
            paramMap.put("ids", collectionList);
            log.info("调用删除向量远程接口 START...");
            long startRemove = System.currentTimeMillis();
            remoteModelService.dropVectors(paramMap);
            log.info("调用删除向量远程接口 END...共耗时 {} 毫秒", System.currentTimeMillis() - startRemove);
            // 调用远程插入接口插入新修改的数据
            JSONObject param = JSONUtil.createObj();
            param.put("collection", knowledgeBase.getCollectionNameQa());
            param.put("question", bizQuestionAnswer.getQuestion());
            param.put("answer", bizQuestionAnswer.getAnswer());
            log.info("调用添加文本问答远程接口 START...");
            long start = System.currentTimeMillis();
            AppendQaResponse appendQaResponse = remoteModelService.appendQa(param);
            if (appendQaResponse.getId() != null) {
                bizQuestionAnswer.setCollectionId(appendQaResponse.getId());
            }
            log.info("调用添加文本问答远程接口 END...共耗时 {} 毫秒", System.currentTimeMillis() - start);
        }
        //更新本地数据库问答数据
        int i = bizQuestionAnswerMapper.updateBizQuestionAnswer(bizQuestionAnswer);
        if (i > 0) {
            return 1;
        }
        return -1;
    }

    /**
     * 批量删除问答库
     *
     * @param ids 需要删除的问答库主键
     * @return 结果
     */
    @Override
    public int deleteBizQuestionAnswerByIds(Long[] ids) {
        return bizQuestionAnswerMapper.deleteBizQuestionAnswerByIds(ids);
    }

    /**
     * 删除问答库信息
     *
     * @param id 问答库主键
     * @return 结果
     */
    @Override
    public int deleteBizQuestionAnswerById(Long id) {
        return bizQuestionAnswerMapper.deleteBizQuestionAnswerById(id);
    }

    @Override
    @Transactional
    public String readExcelFile(MultipartFile[] files, String username, Long digitalEmployeeId, HttpServletRequest request) throws IOException {
        BizDigitalEmployee bizDigitalEmployee = bizDigitalEmployeeMapper.selectBizDigitalEmployeeById(digitalEmployeeId);
        if (bizDigitalEmployee == null) {
            throw new BaseException("该数字员工不存在");
        }
        List<BizQuestionAnswer> newList = new ArrayList<>();
        String result = "";
        for (MultipartFile file : files) {
            //创建处理EXCEL的类
            ReadExcel readExcel = new ReadExcel();
            //解析excel，获取上传的事件单
            List<BizQuestionAnswer> questionAnswersVoList = readExcel.getExcelInfo(file);
            //问答数据数据库查重，如果数据库存在则不插入
            List<BizQuestionAnswer> resList = new ArrayList<>();
            for (BizQuestionAnswer bizQuestionAnswer : questionAnswersVoList) {
                bizQuestionAnswer.setDigitalEmployeeId(digitalEmployeeId);
                bizQuestionAnswer.setCreateBy(username);
                bizQuestionAnswer.setCreateTime(new Date());
                bizQuestionAnswer.setUpdateBy(username);
                bizQuestionAnswer.setUpdateTime(new Date());
                bizQuestionAnswer.setCreateType(0);
                String question = bizQuestionAnswer.getQuestion();
                bizQuestionAnswer.setQuestion(question.replace("？", ""));
                resList.add(bizQuestionAnswer);
            }
            newList.addAll(resList);
            List<Long> idList = new ArrayList<>();
            for (BizQuestionAnswer questionAnswer : resList) {
                if (questionAnswer.getId() != null) {
                    idList.add(questionAnswer.getId());
                }
            }
            //获取问答id的集合
            List<String> stringList = idList.stream().map(String::valueOf).collect(Collectors.toList());
            String s = StringUtils.join(stringList, ",");

            //保存问答文件
            BizDigitalEmployee digitalEmployee = bizDigitalEmployeeMapper.selectBizDigitalEmployeeById(digitalEmployeeId);
            if (digitalEmployee == null) {
                throw new BaseException("该数字员工不存在");
            }
            String savePath = chatResourcesConfig.getKnowledgeFilePath();
            String path = FileUploadUtils.uploadKnowledgeFile(savePath, file);
            BizQuestionFile questionFile = new BizQuestionFile();
            questionFile.setFileName(file.getOriginalFilename());
            questionFile.setFileSize(file.getSize());
            questionFile.setDigitalEmployeeId(digitalEmployeeId);
            questionFile.setFilePath(path);
            questionFile.setCreateBy(username);
            questionFile.setCreateTime(new Date());
            questionFile.setQuestionId(s);
            int i = bizQuestionFileMapper.insertBizQuestionFile(questionFile);
            if (i > 0) {
                result = "上传成功";
            } else {
                result = "上传失败";
            }
        }
        //调用远程上传文件接口
        Long knowledgeBaseId = bizKnowledgeBaseService.getKnowledgeBaseIdByDeId(digitalEmployeeId);
        BizKnowledgeBase knowledgeBase = bizKnowledgeBaseService.getById(knowledgeBaseId);
        if (knowledgeBase == null) {
            throw new RuntimeException("知识库不存在");
        }
        log.info("调用文档上传远程接口 START...");
        long start = System.currentTimeMillis();
        List<String> listnew = new ArrayList<>();
        List<String> list = remoteModelService.readExcel(knowledgeBase.getCollectionNameQa(), files);
        listnew.addAll(list);
        log.info("调用文档上传远程接口 END...共耗时 {} 毫秒", System.currentTimeMillis() - start);
        if (!listnew.isEmpty() && !newList.isEmpty()) {
            for (int i = 0; i < newList.size(); i++) {
                BizQuestionAnswer bizQuestionAnswer = newList.get(i);
                bizQuestionAnswer.setCollectionId(listnew.get(i));
            }
        }
        //问答数据批量插入
        if (!newList.isEmpty()) {
            bizQuestionAnswerMapper.insertBatchQuestionAnswer(newList);
        }
        return result;
    }

    @Override
    public List<BizQuestionAnswer> querySimilarQuestionList(BizQuestionAnswer bizQuestionAnswer, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        List<BizQuestionAnswer> list = new ArrayList<>();
        List<BizQuestionAnswer> bizQuestionAnswerList = bizQuestionAnswerMapper.querySimilarQuestionList(bizQuestionAnswer, startTime, endTime);
        if (!bizQuestionAnswerList.isEmpty()) {
            for (BizQuestionAnswer questionAnswer : bizQuestionAnswerList) {
                list.add(questionAnswer);
            }
        }
        return list;
    }
}
