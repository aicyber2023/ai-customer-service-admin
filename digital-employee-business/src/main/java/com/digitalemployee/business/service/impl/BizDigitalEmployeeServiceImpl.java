package com.digitalemployee.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.business.domain.*;
import com.digitalemployee.business.mapper.*;
import com.digitalemployee.business.modules.chatsession.domain.BizSessionRecord;
import com.digitalemployee.business.modules.chatsession.mapper.BizSessionRecordMapper;
import com.digitalemployee.business.modules.de.domain.BizDigitalEmployeeProcedure;
import com.digitalemployee.business.modules.de.mapper.BizDigitalEmployeeProcedureMapper;
import com.digitalemployee.business.service.IBizDigitalEmployeeService;
import com.digitalemployee.business.service.IBizKnowledgeBaseService;
import com.digitalemployee.common.annotation.DataScope;
import com.digitalemployee.common.core.domain.entity.SysUserDeConfig;
import com.digitalemployee.common.utils.SecurityUtils;
import com.digitalemployee.common.utils.uuid.IdUtils;
import com.digitalemployee.system.mapper.SysUserDeConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 客户数字员工Service业务层处理
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Service
@RequiredArgsConstructor
public class BizDigitalEmployeeServiceImpl extends ServiceImpl<BizDigitalEmployeeMapper, BizDigitalEmployee> implements IBizDigitalEmployeeService {

    private final BizDigitalEmployeeMapper bizDigitalEmployeeMapper;

    private final BizDigitalEmployeeContextMapper bizDigitalEmployeeContextMapper;
    private final BizDigitalEmployeeProcedureMapper bizDigitalEmployeeProcedureMapper;

    private final BizKnowledgeBaseMapper knowledgeBaseMapper;
    private final BizKnowledgeBaseFileMapper knowledgeBaseFileMapper;
    private final BizQuestionFileMapper questionFileMapper;
    private final BizSessionRecordMapper sessionRecordMapper;

    private final SysUserDeConfigMapper sysUserDeConfigMapper;

    private final IBizKnowledgeBaseService bizKnowledgeBaseService;

    /**
     * 查询客户数字员工
     *
     * @param id 客户数字员工主键
     * @return 客户数字员工
     */
    @Override
    public BizDigitalEmployee selectBizDigitalEmployeeById(Long id) {
        BizDigitalEmployee digitalEmployee = bizDigitalEmployeeMapper.selectBizDigitalEmployeeById(id);
        List<BizDigitalEmployeeContext> context = selectContextListByDigitalEmployeeId(id);
        digitalEmployee.setContext(context);
        List<BizDigitalEmployeeProcedure> procedureList = selectProcedureListByDigitalEmployeeId(id);
        digitalEmployee.setProcedureList(procedureList);
        return digitalEmployee;
    }

    /**
     * 查询客户数字员工列表
     *
     * @param bizDigitalEmployee 客户数字员工
     * @return 客户数字员工
     */
    @Override
    @DataScope(deptAlias = "de", userAlias = "de")
    public List<BizDigitalEmployee> selectBizDigitalEmployeeList(BizDigitalEmployee bizDigitalEmployee) {
        return bizDigitalEmployeeMapper.selectBizDigitalEmployeeList(bizDigitalEmployee);
    }

    /**
     * 新增客户数字员工
     *
     * @param bizDigitalEmployee 客户数字员工
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BizDigitalEmployee insertBizDigitalEmployee(BizDigitalEmployee bizDigitalEmployee) {
        Long sysUserId = bizDigitalEmployee.getSysUserId();
        if (this.isExceedDigitalEmployeeAmount(sysUserId)) {
            throw new RuntimeException("已超出用户数字员工个数限制，请联系管理员");
        }
        bizDigitalEmployee.setStatus(0);

        // 初始化知识库
        BizKnowledgeBase knowledgeBase = bizKnowledgeBaseService.initKnowledgeBase(bizDigitalEmployee);
        bizDigitalEmployee.setKnowledgeBaseId(knowledgeBase.getId());

        bizDigitalEmployee.setEmployeeKey(IdUtils.fastUUID());
        bizDigitalEmployeeMapper.insert(bizDigitalEmployee);
        this.saveContext(bizDigitalEmployee);
        this.saveProcedureList(bizDigitalEmployee);
        return bizDigitalEmployee;
    }

    private boolean isExceedDigitalEmployeeAmount(Long sysUserId) {

        LambdaQueryWrapper<SysUserDeConfig> configWrapper = Wrappers.lambdaQuery();
        configWrapper.eq(SysUserDeConfig::getSysUserId, sysUserId);
        SysUserDeConfig sysUserDeConfig = sysUserDeConfigMapper.selectOne(configWrapper);

        if (sysUserDeConfig == null) {
            throw new RuntimeException("用户(sysUserId): " + sysUserId + " 的数字员工配置不存在，请联系管理员");
        }

        Long employeeAmount = sysUserDeConfig.getEmployeeAmount();
        if (employeeAmount == -1) {
            return false;
        }

        LambdaQueryWrapper<BizDigitalEmployee> empWrapper = Wrappers.lambdaQuery();
        empWrapper.eq(BizDigitalEmployee::getSysUserId, sysUserId).ne(BizDigitalEmployee::getStatus, 2);
        long existEmpAmount = this.count(empWrapper);
        return existEmpAmount + 1 > employeeAmount;
    }

    /**
     * 修改客户数字员工
     *
     * @param bizDigitalEmployee 客户数字员工
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBizDigitalEmployee(BizDigitalEmployee bizDigitalEmployee) {
        this.saveContext(bizDigitalEmployee);
        this.saveProcedureList(bizDigitalEmployee);
        return bizDigitalEmployeeMapper.updateById(bizDigitalEmployee);
    }

    /**
     * 批量删除客户数字员工
     *
     * @param ids 需要删除的客户数字员工主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBizDigitalEmployeeByIds(Long[] ids) {
        List<Long> idList = Arrays.asList(ids);
        LambdaQueryWrapper<BizDigitalEmployeeContext> wrapper = Wrappers.lambdaQuery();
        wrapper.in(BizDigitalEmployeeContext::getDigitalEmployeeId, idList);
        bizDigitalEmployeeContextMapper.delete(wrapper);
        return bizDigitalEmployeeMapper.deleteBizDigitalEmployeeByIds(ids);
    }

    @Override
    public int deleteBizDigitalEmployeeByIdsLogic(Long[] ids) {
        List<Long> idList = Arrays.asList(ids);
        idList.forEach(id -> {
            BizDigitalEmployee digitalEmployee = new BizDigitalEmployee();
            digitalEmployee.setId(id);
            digitalEmployee.setStatus(2);
            digitalEmployee.setUpdateTime(new Date());
            digitalEmployee.setUpdateBy(String.valueOf(SecurityUtils.getLoginUser().getUserId()));
            this.updateById(digitalEmployee);
        });
        return idList.size();
    }

    /**
     * 删除客户数字员工信息
     *
     * @param id 客户数字员工主键
     * @return 结果
     */
    @Override
    public int deleteBizDigitalEmployeeById(Long id) {
        return bizDigitalEmployeeMapper.deleteBizDigitalEmployeeById(id);
    }

    @Override
    public void saveAvatar(MultipartFile file, String employeeId) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("不能上传空的头像文件");
        }
        BizDigitalEmployee one = this.getById(employeeId);
        if (one == null) {
            throw new RuntimeException("未找到指定数字员工");
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
    public void saveCompanyAvatar(MultipartFile file, String employeeId) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("不能上传空的头像文件");
        }
        BizDigitalEmployee one = this.getById(employeeId);
        if (one == null) {
            throw new RuntimeException("未找到指定数字员工");
        }
        try {
            one.setCompanyAvatar(file.getBytes());
            one.setCompanyAvatarContentType(file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.updateById(one);
    }

    private LambdaQueryWrapper<BizDigitalEmployeeContext> generateContextWrapper(Long digitalEmployeeId) {
        LambdaQueryWrapper<BizDigitalEmployeeContext> wrapper = Wrappers.lambdaQuery();
        return wrapper.eq(BizDigitalEmployeeContext::getDigitalEmployeeId, digitalEmployeeId);
    }

    private LambdaQueryWrapper<BizDigitalEmployeeProcedure> generateProcedureWrapper(Long digitalEmployeeId) {
        LambdaQueryWrapper<BizDigitalEmployeeProcedure> wrapper = Wrappers.lambdaQuery();
        return wrapper.eq(BizDigitalEmployeeProcedure::getDigitalEmployeeId, digitalEmployeeId);
    }

    private List<BizDigitalEmployeeContext> selectContextListByDigitalEmployeeId(Long digitalEmployeeId) {
        return bizDigitalEmployeeContextMapper.selectList(this.generateContextWrapper(digitalEmployeeId));
    }

    private List<BizDigitalEmployeeProcedure> selectProcedureListByDigitalEmployeeId(Long digitalEmployeeId) {
        return bizDigitalEmployeeProcedureMapper.selectList(this.generateProcedureWrapper(digitalEmployeeId));
    }

    private void saveContext(BizDigitalEmployee bizDigitalEmployee) {
        Long digitalEmployeeId = bizDigitalEmployee.getId();
        Long templateId = bizDigitalEmployee.getTemplateId();
        // 删除旧数据
        bizDigitalEmployeeContextMapper.delete(this.generateContextWrapper(digitalEmployeeId));
        // 保存context
        List<BizDigitalEmployeeContext> context = bizDigitalEmployee.getContext();
        if (CollectionUtils.isNotEmpty(context)) {
            context.forEach(o -> {
                o.setTemplateId(templateId);
                o.setDigitalEmployeeId(digitalEmployeeId);
                o.setDate(new Date());
                bizDigitalEmployeeContextMapper.insert(o);
            });
        }
    }

    private void saveProcedureList(BizDigitalEmployee bizDigitalEmployee) {
        Long digitalEmployeeId = bizDigitalEmployee.getId();
        Long templateId = bizDigitalEmployee.getTemplateId();
        // 删除旧数据
        bizDigitalEmployeeProcedureMapper.delete(this.generateProcedureWrapper(digitalEmployeeId));
        // 保存context
        List<BizDigitalEmployeeProcedure> procedureList = bizDigitalEmployee.getProcedureList();
        if (CollectionUtils.isNotEmpty(procedureList)) {
            procedureList.forEach(o -> {
                o.setTemplateId(templateId);
                o.setDigitalEmployeeId(digitalEmployeeId);
                bizDigitalEmployeeProcedureMapper.insert(o);
            });
        }
    }

    @Override
    public void findDigiEmpData(List<BizDigitalEmployee> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        List<Long> deIdList = list.stream()
                .peek(de -> {
                    de.setAvatar(null);
                    de.setCompanyAvatar(null);
                })
                .map(BizDigitalEmployee::getId).collect(Collectors.toList());

        // 查询服务次数 session count
        LambdaQueryWrapper<BizSessionRecord> sessionRecordWrapper = Wrappers.lambdaQuery();
        sessionRecordWrapper.in(BizSessionRecord::getDigitalEmployeeId, deIdList);
        List<BizSessionRecord> sessionRecordList = sessionRecordMapper.selectList(sessionRecordWrapper);

        Map<Long, List<BizSessionRecord>> seesionMap
                = sessionRecordList.stream().collect(Collectors.groupingBy(BizSessionRecord::getDigitalEmployeeId));
        Map<Long, BizKnowledgeBase> knowledgeBaseMap = new HashMap<>();
        Map<Long, List<BizKnowledgeBaseFile>> fileMap = new HashMap<>();

        // 查询知识库 knowledge list
        List<Long> knowledgeBaseIdList
                = list.stream().map(BizDigitalEmployee::getKnowledgeBaseId).filter(Objects::nonNull).collect(Collectors.toList());
        if (!knowledgeBaseIdList.isEmpty()) {
            LambdaQueryWrapper<BizKnowledgeBase> knowledgeBaseWrapper = Wrappers.lambdaQuery();
            knowledgeBaseWrapper.in(BizKnowledgeBase::getId, knowledgeBaseIdList);
            List<BizKnowledgeBase> baseList = knowledgeBaseMapper.selectList(knowledgeBaseWrapper);
            if (!baseList.isEmpty()) {
                // 查询文件个数 knowledge file count
                LambdaQueryWrapper<BizKnowledgeBaseFile> knowledgeBaseFileWrapper = Wrappers.lambdaQuery();
                knowledgeBaseFileWrapper.in(BizKnowledgeBaseFile::getKnowledgeBaseId, knowledgeBaseIdList);
                List<BizKnowledgeBaseFile> fileList = knowledgeBaseFileMapper.selectList(knowledgeBaseFileWrapper);

                knowledgeBaseMap = baseList.stream().collect(Collectors.toMap(BizKnowledgeBase::getId, o -> o));
                fileMap = fileList.stream().collect(Collectors.groupingBy(BizKnowledgeBaseFile::getKnowledgeBaseId));
            }
        }

        // 查询问答库文件
        LambdaQueryWrapper<BizQuestionFile> questionFileWrapper = Wrappers.lambdaQuery();
        questionFileWrapper.in(BizQuestionFile::getDigitalEmployeeId, deIdList);
        List<BizQuestionFile> questionFileList = questionFileMapper.selectList(questionFileWrapper);
        Map<Long, List<BizQuestionFile>> questionFileMap = questionFileList.stream().collect(Collectors.groupingBy(BizQuestionFile::getDigitalEmployeeId));

        for (BizDigitalEmployee digitalEmployee : list) {
            Long digitalEmployeeId = digitalEmployee.getId();
            Long knowledgeBaseId = digitalEmployee.getKnowledgeBaseId();
            if (knowledgeBaseId != null) {
                if (seesionMap.containsKey(digitalEmployeeId)) {
                    List<BizSessionRecord> deSessionList = seesionMap.get(digitalEmployeeId);
                    digitalEmployee.setServeTime(deSessionList.size());
                } else {
                    digitalEmployee.setServeTime(0);
                }
                if (knowledgeBaseMap.containsKey(knowledgeBaseId)) {
                    BizKnowledgeBase knowledgeBase = knowledgeBaseMap.get(digitalEmployeeId);
                    digitalEmployee.setKnowledgeBase(knowledgeBase);
                } else {
                    digitalEmployee.setKnowledgeBase(null);
                }
                if (fileMap.containsKey(knowledgeBaseId)) {
                    for (Map.Entry<Long, List<BizKnowledgeBaseFile>> longListEntry : fileMap.entrySet()) {
                        Long key = longListEntry.getKey();
                        if (Objects.equals(key, digitalEmployeeId)) {
                            digitalEmployee.setKnowledgeBaseFileCount(longListEntry.getValue().size());
                        }
                        digitalEmployee.setKnowledgeBaseFileCount(0);
                    }


                } else {
                    digitalEmployee.setKnowledgeBaseFileCount(0);
                }
            }

            if (questionFileMap.containsKey(digitalEmployeeId)) {
                List<BizQuestionFile> qaFiles = questionFileMap.get(digitalEmployeeId);
                Integer knowledgeBaseFileCount = digitalEmployee.getKnowledgeBaseFileCount();
                digitalEmployee.setKnowledgeBaseFileCount(knowledgeBaseFileCount + qaFiles.size());
            }

        }

    }

}
