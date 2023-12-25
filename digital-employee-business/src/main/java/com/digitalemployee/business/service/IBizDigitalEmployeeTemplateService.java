package com.digitalemployee.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalemployee.business.domain.BizDigitalEmployeeTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 数字员工模板Service接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
public interface IBizDigitalEmployeeTemplateService extends IService<BizDigitalEmployeeTemplate> {
    /**
     * 查询数字员工模板
     *
     * @param id 数字员工模板主键
     * @return 数字员工模板
     */
    BizDigitalEmployeeTemplate selectBizDigitalEmployeeTemplateById(Long id);

    /**
     * 查询数字员工模板列表
     *
     * @param bizDigitalEmployeeTemplate 数字员工模板
     * @return 数字员工模板集合
     */
    List<BizDigitalEmployeeTemplate> selectBizDigitalEmployeeTemplateList(BizDigitalEmployeeTemplate bizDigitalEmployeeTemplate);

    List<BizDigitalEmployeeTemplate> selectUsedTemplateList(Long sysUserId);

    /**
     * 新增数字员工模板
     *
     * @param bizDigitalEmployeeTemplate 数字员工模板
     * @return 结果
     */
    BizDigitalEmployeeTemplate insertBizDigitalEmployeeTemplate(BizDigitalEmployeeTemplate bizDigitalEmployeeTemplate);

    /**
     * 修改数字员工模板
     *
     * @param bizDigitalEmployeeTemplate 数字员工模板
     * @return 结果
     */
    int updateBizDigitalEmployeeTemplate(BizDigitalEmployeeTemplate bizDigitalEmployeeTemplate);

    /**
     * 批量删除数字员工模板
     *
     * @param ids 需要删除的数字员工模板主键集合
     * @return 结果
     */
    int deleteBizDigitalEmployeeTemplateByIds(Long[] ids);

    /**
     * 删除数字员工模板信息
     *
     * @param id 数字员工模板主键
     * @return 结果
     */
    int deleteBizDigitalEmployeeTemplateById(Long id);

    void saveAvatar(MultipartFile file, String templateId);

    void saveCompanyAvatar(MultipartFile file, String templateId);
}
