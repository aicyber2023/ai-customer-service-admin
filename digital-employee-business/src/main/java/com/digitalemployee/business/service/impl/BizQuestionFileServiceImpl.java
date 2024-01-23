package com.digitalemployee.business.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.business.api.RemoteModelService;
import com.digitalemployee.business.api.domain.BaseResponse;
import com.digitalemployee.business.domain.BizKnowledgeBase;
import com.digitalemployee.business.domain.BizQuestionAnswer;
import com.digitalemployee.business.domain.BizQuestionFile;
import com.digitalemployee.business.domain.BizSimilarityQuestion;
import com.digitalemployee.business.mapper.BizDigitalEmployeeMapper;
import com.digitalemployee.business.mapper.BizQuestionAnswerMapper;
import com.digitalemployee.business.mapper.BizQuestionFileMapper;
import com.digitalemployee.business.mapper.BizSimilarityQuestionMapper;
import com.digitalemployee.business.service.IBizKnowledgeBaseService;
import com.digitalemployee.business.service.IBizQuestionFileService;
import com.digitalemployee.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


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
    public void downInChargeOfTemplate(HttpServletResponse response) {
        responseSetting(response, "问答模板", ".xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // 读取文件的输入流
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("templates/问答模板.xlsx");
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(outputStream);
        }
    }

    @Override
    public void uploadTemplate(MultipartFile file)  {
        if (file==null) {
            log.error("文件不能为空");
            throw new RuntimeException("上传Excel文件内容为空，请重新上传！");
        }
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
            log.error(fileName + "不是Excel文件!");
            throw new RuntimeException(fileName + "不是Excel文件!");
        }
        ClassLoader classLoader = getClass().getClassLoader();
        URL resourceURL = classLoader.getResource("templates");
        String filePath = resourceURL.getPath();

        String resourcePath = filePath.replace("target/classes/", "");
        //保存文件到本地
        File dir1 = new File(resourcePath);
        if (!dir1.exists()) {
            dir1.mkdirs();
        }
        File file1 = new File(dir1.getAbsolutePath() + File.separator  +"问答模板.xlsx");
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void responseSetting(HttpServletResponse response, String fileName, String suffix, String contentType) {
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String newFileName = null;
        try {
            newFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 当客户端请求的资源是一个可下载的资源（这里的“可下载”是指浏览器会弹出下载框或者下载界面）时，对这个可下载资源的描述（例如下载框中的文件名称）就是来源于该头域。
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + newFileName + suffix);
        // 服务器告诉浏览器它发送的数据属于什么文件类型，也就是响应数据的MIME类型
        response.setContentType(contentType);
        response.setCharacterEncoding("utf-8");
        // 关闭缓存（HTTP/1.1）
        response.setHeader("Cache-Control", "no-store");
        // 关闭缓存（HTTP/1.0）
        response.setHeader("Pragma", "no-cache");
        // 缓存有效时间
        response.setDateHeader("Expires", 0);
    }

}
