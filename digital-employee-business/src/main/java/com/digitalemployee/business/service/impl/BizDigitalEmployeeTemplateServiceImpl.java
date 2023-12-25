package com.digitalemployee.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.business.domain.BizDigitalEmployee;
import com.digitalemployee.business.domain.BizDigitalEmployeeTemplate;
import com.digitalemployee.business.domain.BizDigitalEmployeeTemplateContext;
import com.digitalemployee.business.mapper.BizDigitalEmployeeMapper;
import com.digitalemployee.business.mapper.BizDigitalEmployeeTemplateContextMapper;
import com.digitalemployee.business.mapper.BizDigitalEmployeeTemplateMapper;
import com.digitalemployee.business.service.IBizDigitalEmployeeTemplateService;
import com.digitalemployee.common.exception.base.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 数字员工模板Service业务层处理
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Service
@RequiredArgsConstructor
public class BizDigitalEmployeeTemplateServiceImpl extends ServiceImpl<BizDigitalEmployeeTemplateMapper, BizDigitalEmployeeTemplate> implements IBizDigitalEmployeeTemplateService {

    private final BizDigitalEmployeeTemplateMapper bizDigitalEmployeeTemplateMapper;

    private final BizDigitalEmployeeTemplateContextMapper bizDigitalEmployeeTemplateContextMapper;

    private final BizDigitalEmployeeMapper digitalEmployeeMapper;

    /**
     * 查询数字员工模板
     *
     * @param id 数字员工模板主键
     * @return 数字员工模板
     */
    @Override
    public BizDigitalEmployeeTemplate selectBizDigitalEmployeeTemplateById(Long id) {
        BizDigitalEmployeeTemplate template = bizDigitalEmployeeTemplateMapper.selectBizDigitalEmployeeTemplateById(id);
        List<BizDigitalEmployeeTemplateContext> contextList = selectContextListByTemplateId(id);
        template.setContext(contextList);
        return template;
    }

    /**
     * 查询数字员工模板列表
     *
     * @param bizDigitalEmployeeTemplate 数字员工模板
     * @return 数字员工模板
     */
    @Override
    public List<BizDigitalEmployeeTemplate> selectBizDigitalEmployeeTemplateList(BizDigitalEmployeeTemplate bizDigitalEmployeeTemplate) {
        return bizDigitalEmployeeTemplateMapper.selectBizDigitalEmployeeTemplateList(bizDigitalEmployeeTemplate);
    }

    @Override
    public List<BizDigitalEmployeeTemplate> selectUsedTemplateList(Long sysUserId) {
        return bizDigitalEmployeeTemplateMapper.selectUsedTemplateList(sysUserId);
    }

    /**
     * 新增数字员工模板
     *
     * @param bizDigitalEmployeeTemplate 数字员工模板
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BizDigitalEmployeeTemplate insertBizDigitalEmployeeTemplate(BizDigitalEmployeeTemplate bizDigitalEmployeeTemplate) {
        bizDigitalEmployeeTemplateMapper.insertBizDigitalEmployeeTemplate(bizDigitalEmployeeTemplate);
        this.saveTemplateContext(bizDigitalEmployeeTemplate);
        return bizDigitalEmployeeTemplate;
    }

    /**
     * 修改数字员工模板
     *
     * @param bizDigitalEmployeeTemplate 数字员工模板
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBizDigitalEmployeeTemplate(BizDigitalEmployeeTemplate bizDigitalEmployeeTemplate) {
        this.saveTemplateContext(bizDigitalEmployeeTemplate);
        return bizDigitalEmployeeTemplateMapper.updateBizDigitalEmployeeTemplate(bizDigitalEmployeeTemplate);
    }

    /**
     * 批量删除数字员工模板
     *
     * @param ids 需要删除的数字员工模板主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBizDigitalEmployeeTemplateByIds(Long[] ids) {
        List<Long> idList = Arrays.asList(ids);

        if (isUsed(idList)) {
            throw new BaseException("数字员工模块使用中的模板无法删除！");
        }

        LambdaQueryWrapper<BizDigitalEmployeeTemplateContext> wrapper = Wrappers.lambdaQuery();
        wrapper.in(BizDigitalEmployeeTemplateContext::getTemplateId, idList);
        bizDigitalEmployeeTemplateContextMapper.delete(wrapper);
        return bizDigitalEmployeeTemplateMapper.deleteBizDigitalEmployeeTemplateByIds(ids);
    }

    private boolean isUsed(List<Long> idList) {
        LambdaQueryWrapper<BizDigitalEmployee> wrapper = Wrappers.lambdaQuery();
        wrapper.in(BizDigitalEmployee::getTemplateId, idList);
        return digitalEmployeeMapper.selectCount(wrapper) > 0;
    }

    /**
     * 删除数字员工模板信息
     *
     * @param id 数字员工模板主键
     * @return 结果
     */
    @Override
    public int deleteBizDigitalEmployeeTemplateById(Long id) {
        return bizDigitalEmployeeTemplateMapper.deleteBizDigitalEmployeeTemplateById(id);
    }

    @Override
    public void saveAvatar(MultipartFile file, String templateId) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("不能上传空的头像文件");
        }
        BizDigitalEmployeeTemplate one = this.getById(templateId);
        if (one == null) {
            throw new RuntimeException("未找到指定模板");
        }
        try {
            one.setAvatar(file.getBytes());
            one.setAvatarContentType(file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.updateById(one);
    }

    @Override
    public void saveCompanyAvatar(MultipartFile file, String templateId) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("不能上传空的头像文件");
        }
        BizDigitalEmployeeTemplate one = this.getById(templateId);
        if (one == null) {
            throw new RuntimeException("未找到指定模板");
        }
        try {
            one.setCompanyAvatar(file.getBytes());
            one.setCompanyAvatarContentType(file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.updateById(one);
    }

    public LambdaQueryWrapper<BizDigitalEmployeeTemplateContext> generateContextWrapper(Long templateId) {
        LambdaQueryWrapper<BizDigitalEmployeeTemplateContext> wrapper = Wrappers.lambdaQuery();
        return wrapper.eq(BizDigitalEmployeeTemplateContext::getTemplateId, templateId);
    }

    public List<BizDigitalEmployeeTemplateContext> selectContextListByTemplateId(Long templateId) {
        return bizDigitalEmployeeTemplateContextMapper.selectList(this.generateContextWrapper(templateId));
    }

    public void saveTemplateContext(BizDigitalEmployeeTemplate bizDigitalEmployeeTemplate) {
        Long templateId = bizDigitalEmployeeTemplate.getId();
        // 删除旧数据
        bizDigitalEmployeeTemplateContextMapper.delete(this.generateContextWrapper(templateId));
        // 保存context
        List<BizDigitalEmployeeTemplateContext> context = bizDigitalEmployeeTemplate.getContext();
        if (CollectionUtils.isNotEmpty(context)) {
            context.forEach(o -> {
                o.setTemplateId(templateId);
                o.setDate(new Date());
                bizDigitalEmployeeTemplateContextMapper.insert(o);
            });
        }
    }

}
