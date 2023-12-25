package com.digitalemployee.business.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.business.api.BizWebSocket;
import com.digitalemployee.business.domain.BizDigitalEmployee;
import com.digitalemployee.business.domain.BizKnowledgeBase;
import com.digitalemployee.business.domain.BizKnowledgeBaseFile;
import com.digitalemployee.business.mapper.BizDigitalEmployeeMapper;
import com.digitalemployee.business.mapper.BizKnowledgeBaseFileMapper;
import com.digitalemployee.business.mapper.BizKnowledgeBaseMapper;
import com.digitalemployee.business.modules.config.ChatResourcesConfig;
import com.digitalemployee.business.modules.knowledgebase.domain.ChatRemoteResponse;
import com.digitalemployee.business.modules.knowledgebase.domain.KnowledgeUploadResponse;
import com.digitalemployee.business.service.IBizKnowledgeBaseFileService;
import com.digitalemployee.business.service.IBizKnowledgeBaseService;
import com.digitalemployee.common.annotation.DataScope;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.core.domain.entity.SysUserDeConfig;
import com.digitalemployee.common.exception.base.BaseException;
import com.digitalemployee.common.utils.file.FileUploadUtils;
import com.digitalemployee.system.mapper.SysUserDeConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 知识库Service业务层处理
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Service
@RequiredArgsConstructor
public class BizKnowledgeBaseServiceImpl extends ServiceImpl<BizKnowledgeBaseMapper, BizKnowledgeBase> implements IBizKnowledgeBaseService {

    private final ChatResourcesConfig chatResourcesConfig;

    private final BizKnowledgeBaseMapper bizKnowledgeBaseMapper;

    private final BizKnowledgeBaseFileMapper bizKnowledgeBaseFileMapper;

    private final SysUserDeConfigMapper sysUserDeConfigMapper;

    private final BizDigitalEmployeeMapper bizDigitalEmployeeMapper;

    private final IBizKnowledgeBaseFileService bizKnowledgeBaseFileService;

    private final BizWebSocket bizWebSocket;


    /**
     * 查询知识库
     *
     * @param id 知识库主键
     * @return 知识库
     */
    @Override
    public BizKnowledgeBase selectBizKnowledgeBaseById(Long id) {
        BizKnowledgeBase knowledgeBase = bizKnowledgeBaseMapper.selectBizKnowledgeBaseById(id);
        LambdaQueryWrapper<BizKnowledgeBaseFile> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BizKnowledgeBaseFile::getKnowledgeBaseId, id);
        List<BizKnowledgeBaseFile> list = bizKnowledgeBaseFileService.list(wrapper);
        knowledgeBase.setFileList(list);
        return knowledgeBase;
    }

    /**
     * 查询知识库列表
     *
     * @param bizKnowledgeBase 知识库
     * @return 知识库
     */
    @Override
    @DataScope(deptAlias = "kb", userAlias = "kb")
    public List<BizKnowledgeBase> selectBizKnowledgeBaseList(BizKnowledgeBase bizKnowledgeBase) {
        return bizKnowledgeBaseMapper.selectBizKnowledgeBaseList(bizKnowledgeBase);
    }

    /**
     * 新增知识库
     *
     * @param bizKnowledgeBase 知识库
     * @return 结果
     */
    @Override
    public int insertBizKnowledgeBase(BizKnowledgeBase bizKnowledgeBase) {
        if (isExceedKnowledgeBaseAmount(bizKnowledgeBase.getSysUserId())) {
            throw new RuntimeException("知识库数量超过限制，请联系管理员");
        }
        return bizKnowledgeBaseMapper.insert(bizKnowledgeBase);
    }

    /**
     * 修改知识库
     *
     * @param bizKnowledgeBase 知识库
     * @return 结果
     */
    @Override
    public int updateBizKnowledgeBase(BizKnowledgeBase bizKnowledgeBase) {
        return bizKnowledgeBaseMapper.updateById(bizKnowledgeBase);
    }

    /**
     * 批量删除知识库
     *
     * @param ids 需要删除的知识库主键
     * @return 结果
     */
    @Override
    public int deleteBizKnowledgeBaseByIds(Long[] ids) {
        return bizKnowledgeBaseMapper.deleteBizKnowledgeBaseByIds(ids);
    }

    /**
     * 删除知识库信息
     *
     * @param id 知识库主键
     * @return 结果
     */
    @Override
    public int deleteBizKnowledgeBaseById(Long id) {
        return bizKnowledgeBaseMapper.deleteBizKnowledgeBaseById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult uploadFile(Long digitalEmployeeId, MultipartFile[] files) {
        Long knowledgeBaseId = this.getKnowledgeBaseIdByDeId(digitalEmployeeId);

        BizKnowledgeBase knowledgeBase = this.getById(knowledgeBaseId);
        if (knowledgeBase == null) {
            throw new RuntimeException("知识库不存在");
        }

        // 文档数量验证
        if (isExceedFileAmount(knowledgeBaseId, knowledgeBase.getSysUserId(), files.length)) {
            throw new RuntimeException("知识库文档数量超过限制，请联系管理员");
        }

        // TODO: 2023/10/23 计算文档文字数量(word, pdf, txt)
        int newWordAmount = this.countWordAmount(files);
        // 文字数量验证
        if (isExceedWordAmount(knowledgeBaseId, knowledgeBase.getSysUserId(), newWordAmount)) {
            throw new RuntimeException("知识库文档文字数量超过限制，请联系管理员");
        }

        try {
            List<BizKnowledgeBaseFile> fileList = new ArrayList<>();
            String savePath = chatResourcesConfig.getKnowledgeFilePath();
//            List<Long> fileIds =  new ArrayList<>();
            Map<String, Long> fileIdsMap = new HashMap<>();
            for (MultipartFile file : files) {
                // 保存文件
                File saveFile = new File(savePath);
                if (!saveFile.exists()) {
                    saveFile.mkdir();
                }
                String s = FileUploadUtils.uploadKnowledgeFile(savePath, file);

                // 保存文件数据
                BizKnowledgeBaseFile knowledgeBaseFile = new BizKnowledgeBaseFile();
                knowledgeBaseFile.setKnowledgeBaseId(knowledgeBaseId);
                knowledgeBaseFile.setFileName(file.getOriginalFilename());
                knowledgeBaseFile.setFilePath(s);
                knowledgeBaseFile.setStatus(0);
                knowledgeBaseFile.setCreateTime(new Date());
                knowledgeBaseFile.setCreateBy(knowledgeBase.getCreateBy());

                knowledgeBaseFile.setFileSize(file.getSize());
                fileList.add(knowledgeBaseFile);
            }
            bizKnowledgeBaseFileService.saveBatch(fileList);
            for (BizKnowledgeBaseFile bizKnowledgeBaseFile : fileList) {
                fileIdsMap.put("fileId", bizKnowledgeBaseFile.getId());
            }
            return AjaxResult.success(fileIdsMap);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean appendFileToKnowledgeBase(Long knowledgeFileId) {
        BizKnowledgeBaseFile knowledgeBaseFile = bizKnowledgeBaseFileService.getById(knowledgeFileId);
        BizKnowledgeBase knowledgeBase = this.getById(knowledgeBaseFile.getKnowledgeBaseId());
        String collectionName = knowledgeBase.getCollectionName();


        this.saveAndSendMessage(knowledgeBaseFile, 2);
//
//        Map<String, Object> paramMap = new HashMap<>();
//        paramMap.put("file", new File(filePath));
//        try(HttpResponse execute = HttpRequest.post( chatResourcesConfig.getUploadTextUrl()+ collectionName)
//                .header(Header.CONTENT_TYPE, "multipart/form-data")//头信息，多个头信息多次调用此方法即可
//                .form(paramMap)//表单内容
//                .timeout(20000)//超时，毫秒
//                .execute()){
//            String result = execute.body();
//            KnowledgeUploadResponse response = JSONUtil.toBean(result, KnowledgeUploadResponse.class);
//
//            if (!response.getSuccessful()) {
//                knowledgeBaseFile.setStatus(2);
//                bizKnowledgeBaseFileService.updateById(knowledgeBaseFile);
//                throw new RuntimeException("远程服务调用异常：" + response.getMessage());
//            }
//
//            knowledgeBaseFile.setStatus(1);
//            knowledgeBaseFile.setVectorIds(response.getIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
//            return bizKnowledgeBaseFileService.updateById(knowledgeBaseFile);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        this.appendFile(knowledgeBaseFile, collectionName);
        return Boolean.TRUE;
    }

    @Override
    @Async
    public void appendFile(BizKnowledgeBaseFile knowledgeBaseFile, String collectionName) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("file", new File(knowledgeBaseFile.getFilePath()));

        try (HttpResponse execute = HttpRequest.post(chatResourcesConfig.getUploadTextUrl() + collectionName)
                .header(Header.CONTENT_TYPE, "multipart/form-data")//头信息，多个头信息多次调用此方法即可
                .form(paramMap)//表单内容
                .timeout(20000)//超时，毫秒
                .execute()) {
            String result = execute.body();
            KnowledgeUploadResponse response = JSONUtil.toBean(result, KnowledgeUploadResponse.class);

            if (!response.getSuccessful()) {
                throw new RuntimeException("远程服务调用异常：" + response.getMessage());
            }

            knowledgeBaseFile.setVectorIds(response.getIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
            this.saveAndSendMessage(knowledgeBaseFile, 1);
        } catch (Exception e) {
            this.saveAndSendMessage(knowledgeBaseFile, 3);
            throw new RuntimeException(e);
        }
    }

    private void saveAndSendMessage(BizKnowledgeBaseFile knowledgeBaseFile, int status) {
        knowledgeBaseFile.setStatus(status);
        bizKnowledgeBaseFileService.updateById(knowledgeBaseFile);
        bizWebSocket.onMessage(knowledgeBaseFile.getId(), JSONUtil.toJsonStr(knowledgeBaseFile));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeFile(Long[] ids) {
        for (Long id : ids) {
            BizKnowledgeBaseFile knowledgeBaseFile = bizKnowledgeBaseFileService.getById(id);
            if (knowledgeBaseFile == null) {
                throw new RuntimeException("知识库文件不存在或已删除");
            }
            Long knowledgeBaseId = knowledgeBaseFile.getKnowledgeBaseId();
            BizKnowledgeBase knowledgeBase = this.getById(knowledgeBaseId);
            if (knowledgeBase == null) {
                throw new RuntimeException("知识库已被删除: " + knowledgeBaseId);
            }
            String vectorIds = knowledgeBaseFile.getVectorIds();
            // 删除向量
            if (knowledgeBaseFile.getStatus() == 1 && StrUtil.isNotEmpty(vectorIds)) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("collection", knowledgeBase.getCollectionName());

                String[] vectorIdArr = vectorIds.split(",");
                List<Long> idList = Stream.of(vectorIdArr).map(Long::valueOf).collect(Collectors.toList());
                paramMap.put("ids", idList);
                String jsonStr = JSONUtil.toJsonStr(paramMap);

                try (HttpResponse result = HttpRequest.post(chatResourcesConfig.getDropVectorsUrl())
                        .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                        .body(jsonStr)//表单内容
                        .execute()) {
                    String body = result.body();
                    ChatRemoteResponse response = JSONUtil.toBean(body, ChatRemoteResponse.class);
                    if (!response.getSuccessful()) {
                        throw new RuntimeException("远程服务调用异常：" + response.getMessage());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            // 删除数据和文件
            String filePath = knowledgeBaseFile.getFilePath();
            FileUtil.del(filePath);
        }
        return bizKnowledgeBaseFileService.deleteBizKnowledgeBaseFileByIds(ids);
//        return bizKnowledgeBaseFileService.removeById(knowledgeFileId);
    }

    private SysUserDeConfig selectSysUserDeConfigBySysUserId(Long sysUserId) {
        LambdaQueryWrapper<SysUserDeConfig> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysUserDeConfig::getSysUserId, sysUserId);
        return sysUserDeConfigMapper.selectOne(wrapper);
    }

    private boolean isExceedKnowledgeBaseAmount(Long sysUserId) {
        SysUserDeConfig sysUserDeConfig = this.selectSysUserDeConfigBySysUserId(sysUserId);

        if (sysUserDeConfig == null) {
            throw new RuntimeException("用户(sysUserId): " + sysUserId + " 的知识库配置不存在，请联系管理员");
        }

        Long knowledgeBaseAmount = sysUserDeConfig.getKnowledgeBaseAmount();

        if (knowledgeBaseAmount == -1) {
            return false;
        }

        LambdaQueryWrapper<BizKnowledgeBase> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BizKnowledgeBase::getSysUserId, sysUserId);
        List<BizKnowledgeBase> list = this.list(wrapper);

        return list.size() + 1 > knowledgeBaseAmount;
    }

    private boolean isExceedFileAmount(Long knowledgeBaseId, Long sysUserId, int newFileAmount) {
        SysUserDeConfig sysUserDeConfig = this.selectSysUserDeConfigBySysUserId(sysUserId);

        if (sysUserDeConfig == null) {
            throw new RuntimeException("用户(sysUserId): " + sysUserId + " 的知识库配置不存在，请联系管理员");
        }

        Long knowledgeBaseDocAmount = sysUserDeConfig.getKnowledgeBaseDocAmount();

        if (knowledgeBaseDocAmount == -1) {
            return false;
        }

        LambdaQueryWrapper<BizKnowledgeBaseFile> fileWrapper = Wrappers.lambdaQuery();
        fileWrapper.eq(BizKnowledgeBaseFile::getKnowledgeBaseId, knowledgeBaseId);
        List<BizKnowledgeBaseFile> fileList = bizKnowledgeBaseFileService.list(fileWrapper);

        return fileList.size() + newFileAmount > knowledgeBaseDocAmount;
    }

    private boolean isExceedWordAmount(Long knowledgeBaseId, Long sysUserId, int newWordAmount) {
        SysUserDeConfig sysUserDeConfig = this.selectSysUserDeConfigBySysUserId(sysUserId);

        if (sysUserDeConfig == null) {
            throw new RuntimeException("用户(sysUserId): " + sysUserId + " 的知识库配置不存在，请联系管理员");
        }

        Long knowledgeBaseDocWordAmount = sysUserDeConfig.getKnowledgeBaseDocWordAmount();

        if (knowledgeBaseDocWordAmount == -1) {
            return false;
        }

        LambdaQueryWrapper<BizKnowledgeBaseFile> fileWrapper = Wrappers.lambdaQuery();
        fileWrapper.eq(BizKnowledgeBaseFile::getKnowledgeBaseId, knowledgeBaseId);
        List<BizKnowledgeBaseFile> fileList = bizKnowledgeBaseFileService.list(fileWrapper);

        int sum = fileList.stream().mapToInt(BizKnowledgeBaseFile::getWordAmount).sum();

        return sum + newWordAmount > knowledgeBaseDocWordAmount;
    }

    /**
     * 计算文件文字数量
     *
     * @param files files
     * @return 文字数量
     */
    private int countWordAmount(MultipartFile[] files) {
        // TODO: 2023/10/23 计算文档文字数量(word, pdf, txt)
        return 0;
    }


    @Override
    public BizKnowledgeBase initKnowledgeBase(BizDigitalEmployee bizDigitalEmployee) {
        Long userId = bizDigitalEmployee.getUserId();
        Long deptId = bizDigitalEmployee.getDeptId();

        BizKnowledgeBase bizKnowledgeBase = new BizKnowledgeBase();
        bizKnowledgeBase.setCreateTime(new Date());
        bizKnowledgeBase.setCreateBy(String.valueOf(userId));
        bizKnowledgeBase.setStatus(0);
        bizKnowledgeBase.setSysUserId(userId);
        bizKnowledgeBase.setDeptId(deptId);
        bizKnowledgeBase.setUserId(userId);

        String uuid = IdUtil.fastSimpleUUID();
        bizKnowledgeBase.setCollectionName(uuid);
        bizKnowledgeBase.setCollectionNameQa(IdUtil.fastSimpleUUID());
        bizKnowledgeBase.setName(uuid);
        bizKnowledgeBase.setUpdateBy(String.valueOf(userId));
        this.save(bizKnowledgeBase);
        return bizKnowledgeBase;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<BizKnowledgeBaseFile> selectKbFileList(BizKnowledgeBaseFile knowledgeBaseFile) {
        /*Long digitalEmployeeId = knowledgeBaseFile.getDigitalEmployeeId();
        if (digitalEmployeeId == null) {
            throw new BaseException("参数：数字员工ID 不能为空");
        }

        Integer status = knowledgeBaseFile.getStatus();
        String fileName = knowledgeBaseFile.getFileName();
        Date startDateTime = knowledgeBaseFile.getStartDateTime();
        Date endDateTime = knowledgeBaseFile.getEndDateTime();


        Long knowledgeBaseId = this.getKnowledgeBaseIdByDeId(digitalEmployeeId);
        LambdaQueryWrapper<BizKnowledgeBaseFile> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BizKnowledgeBaseFile::getKnowledgeBaseId, knowledgeBaseId)
                .eq(Objects.nonNull(status), BizKnowledgeBaseFile::getStatus, status)
                .like(StrUtil.isNotEmpty(fileName), BizKnowledgeBaseFile::getFileName, fileName)
                .between(Objects.nonNull(startDateTime) && Objects.nonNull(endDateTime), BizKnowledgeBaseFile::getCreateTime, startDateTime, endDateTime);
        */

        return bizKnowledgeBaseFileMapper.selectBizKnowledgeBaseFileList(knowledgeBaseFile);
    }

    @Override
    public Long getKnowledgeBaseIdByDeId(Long digitalEmployeeId) {
        BizDigitalEmployee digitalEmployee = bizDigitalEmployeeMapper.selectById(digitalEmployeeId);
        if (digitalEmployee == null) {
            throw new BaseException("数据异常，请重试");
        }
        Long knowledgeBaseId = digitalEmployee.getKnowledgeBaseId();
        if (knowledgeBaseId == null) {
            BizKnowledgeBase knowledgeBase = this.initKnowledgeBase(digitalEmployee);
            digitalEmployee.setKnowledgeBaseId(knowledgeBase.getId());
            bizDigitalEmployeeMapper.updateById(digitalEmployee);
            knowledgeBaseId = knowledgeBase.getId();
        }
        return knowledgeBaseId;
    }

}
