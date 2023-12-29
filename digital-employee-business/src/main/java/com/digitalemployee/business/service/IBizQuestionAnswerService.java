package com.digitalemployee.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalemployee.business.domain.BizQuestionAnswer;
import org.apache.ibatis.annotations.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 问答库Service接口
 * 
 * @author aicyber
 * @date 2023-11-27
 */
public interface IBizQuestionAnswerService extends IService<BizQuestionAnswer>
{
    /**
     * 查询问答库
     * 
     * @param id 问答库主键
     * @return 问答库
     */
    BizQuestionAnswer selectBizQuestionAnswerById(Long id);

    /**
     * 查询问答库列表
     * 
     * @param bizQuestionAnswer 问答库
     * @return 问答库集合
     */
    List<BizQuestionAnswer> selectBizQuestionAnswerList(@Param("bizQuestionAnswer") BizQuestionAnswer bizQuestionAnswer,
                                                        @Param("startTime") Date startTime,
                                                        @Param("endTime") Date endTime);

    /**
     * 新增问答库
     * 
     * @param bizQuestionAnswer 问答库
     * @return 结果
     */
    int insertBizQuestionAnswer(BizQuestionAnswer bizQuestionAnswer);

    /**
     * 修改问答库
     * 
     * @param bizQuestionAnswer 问答库
     * @return 结果
     */
    int updateBizQuestionAnswer(BizQuestionAnswer bizQuestionAnswer);

    /**
     * 批量删除问答库
     * 
     * @param ids 需要删除的问答库主键集合
     * @return 结果
     */
    int deleteBizQuestionAnswerByIds(Long[] ids);

    /**
     * 删除问答库信息
     * 
     * @param id 问答库主键
     * @return 结果
     */
    int deleteBizQuestionAnswerById(Long id);

    /**
     * 读取excel中的数据,生成list
     */
    String readExcelFile(@Param("files") MultipartFile[] files, @Param("username") String username,@Param("digitalEmployeeId") Long digitalEmployeeId, HttpServletRequest request) throws IOException;

    List<BizQuestionAnswer> querySimilarQuestionList();
}
