package com.digitalemployee.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalemployee.business.domain.BizQuestionFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 问答库文档Service接口
 * 
 * @author aicyber
 * @date 2023-11-29
 */
public interface IBizQuestionFileService extends IService<BizQuestionFile>
{
    /**
     * 查询问答库文档
     * 
     * @param id 问答库文档主键
     * @return 问答库文档
     */
    BizQuestionFile selectBizQuestionFileById(Long id);

    /**
     * 查询问答库文档列表
     * 
     * @param bizQuestionFile 问答库文档
     * @return 问答库文档集合
     */
    List<BizQuestionFile> selectBizQuestionFileList(BizQuestionFile bizQuestionFile);

    /**
     * 新增问答库文档
     * 
     * @param bizQuestionFile 问答库文档
     * @return 结果
     */
    int insertBizQuestionFile(BizQuestionFile bizQuestionFile);


    /**
     * 修改问答库文档
     * 
     * @param bizQuestionFile 问答库文档
     * @return 结果
     */
    int updateBizQuestionFile(BizQuestionFile bizQuestionFile);

    /**
     * 批量删除问答库文档
     * 
     * @param ids 需要删除的问答库文档主键集合
     * @return 结果
     */
    int deleteBizQuestionFileByIds(Long[] ids);

    /**
     * 删除问答库文档信息
     * 
     * @param id 问答库文档主键
     * @return 结果
     */
    int deleteBizQuestionFileById(Long id);

     void downInChargeOfTemplate(HttpServletResponse response);

    void uploadTemplate(MultipartFile file) throws IOException;
}
