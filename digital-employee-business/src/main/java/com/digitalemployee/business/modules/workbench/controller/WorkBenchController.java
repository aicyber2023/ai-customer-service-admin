package com.digitalemployee.business.modules.workbench.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digitalemployee.business.domain.BizDigitalEmployee;
import com.digitalemployee.business.domain.BizSessionRecord;
import com.digitalemployee.business.domain.BizUser;
import com.digitalemployee.business.mapper.BizDigitalEmployeeMapper;
import com.digitalemployee.business.mapper.BizKnowledgeBaseFileMapper;
import com.digitalemployee.business.modules.workbench.domain.ChatResourceVO;
import com.digitalemployee.business.service.IBizSessionRecordService;
import com.digitalemployee.business.service.IBizUserService;
import com.digitalemployee.common.core.controller.BaseController;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.core.domain.entity.SysUserDeConfig;
import com.digitalemployee.system.service.ISysUserDeConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/de/workbench")
@RequiredArgsConstructor
public class WorkBenchController extends BaseController {

    private final ISysUserDeConfigService sysUserDeConfigService;


    private final BizKnowledgeBaseFileMapper knowledgeBaseFileMapper;

    private final BizDigitalEmployeeMapper digitalEmployeeMapper;

    private final IBizSessionRecordService sessionRecordService;

    private final IBizUserService bizUserService;

    @GetMapping("/chatResource")
    public AjaxResult chatResource() {
        Long userId = getUserId();
        LambdaQueryWrapper<SysUserDeConfig> userDeConfigWrapper = Wrappers.lambdaQuery();
        userDeConfigWrapper.eq(SysUserDeConfig::getSysUserId, userId);
        SysUserDeConfig sysUserDeConfig = sysUserDeConfigService.getOne(userDeConfigWrapper);

        // 用户数字已新建员工数量
        LambdaQueryWrapper<BizDigitalEmployee> digitalEmployeeWrapper = Wrappers.lambdaQuery();
        digitalEmployeeWrapper.eq(BizDigitalEmployee::getSysUserId, userId).ne(BizDigitalEmployee::getStatus, 2);
        Long usedEmployeeAmount = digitalEmployeeMapper.selectCount(digitalEmployeeWrapper);

        // 用户数字已上传文档数量
        Long sysUserKbDocCount = knowledgeBaseFileMapper.selectSysUserKbDocCount(userId);

        return AjaxResult.success(new ChatResourceVO(sysUserDeConfig, usedEmployeeAmount, sysUserKbDocCount));
    }

    @GetMapping("/digitalEmployee")
    public AjaxResult digitalEmployee() {
        // 查询 sysUser 的 数字员工列表
        Long userId = getUserId();
        LambdaQueryWrapper<BizDigitalEmployee> digitalEmployeeWrapper = Wrappers.lambdaQuery();
        digitalEmployeeWrapper.eq(BizDigitalEmployee::getSysUserId, userId).ne(BizDigitalEmployee::getStatus, 2);
        List<BizDigitalEmployee> digitalEmployeeList = digitalEmployeeMapper.selectList(digitalEmployeeWrapper);

        // 用户无数字员工时，直接返回
        if (digitalEmployeeList == null || digitalEmployeeList.isEmpty()) {
            return AjaxResult.success();
        }

        // 初始化 数字化员工 的会话Map
        Map<Long, List<BizSessionRecord>> sessionMapByDeId = new HashMap<>();

        // 查询当前登陆sysUser对应的BizUser
        LambdaQueryWrapper<BizUser> bizUserWrapper = Wrappers.lambdaQuery();
        bizUserWrapper.eq(BizUser::getSysUserId, userId);
        List<BizUser> bizUserList = bizUserService.list(bizUserWrapper);

        // 添加游客用户id
        BizUser user = new BizUser();
        user.setId(0L);
        bizUserList.add(user);

        // 查询用户今日的会话数据，并按 数字员工ID 分组
        // if (bizUserList != null && !bizUserList.isEmpty()) {}

        List<Long> bizUserIdList = bizUserList.stream().map(BizUser::getId).collect(Collectors.toList());
        LambdaQueryWrapper<BizSessionRecord> sessionRecordWrapper = Wrappers.lambdaQuery();
        sessionRecordWrapper
                .in(BizSessionRecord::getUserId, bizUserIdList) // sysUser 对应的 bizUser
                .between(BizSessionRecord::getSendTime,
                        LocalDateTime.now().withHour(0).withMinute(0).withSecond(0),
                        LocalDateTime.now().withHour(23).withMinute(59).withSecond(59)
                ); // 发送时间为今日
        List<BizSessionRecord> sessionRecordList = sessionRecordService.list(sessionRecordWrapper);
        // 按 数字员工ID 分组
        if (sessionRecordList != null && !sessionRecordList.isEmpty()) {
            sessionMapByDeId
                    = sessionRecordList.stream().collect(Collectors.groupingBy(BizSessionRecord::getDigitalEmployeeId));
        }

        // 设置数字员工的 今日服务次数 和 今日服务人数
        for (BizDigitalEmployee de : digitalEmployeeList) {
            Long digitalEmpId = de.getId();
            if (sessionMapByDeId.containsKey(digitalEmpId)) {
                List<BizSessionRecord> deRecordList = sessionMapByDeId.get(digitalEmpId);
                de.setServeTimeToday(deRecordList.size());

                // long userCount = deRecordList.stream().mapToLong(BizSessionRecord::getUserId).distinct().count();
                Set<String> userCountSet = new HashSet<>();
                deRecordList.forEach(deRecord -> {
                    String uId = deRecord.getUserId().toString();
                    if ("0".equals(uId)) {
                        uId = deRecord.getIp();
                    }
                    userCountSet.add(uId);
                });
                de.setServeUserToday(userCountSet.size());
            } else {
                de.setServeTimeToday(0);
                de.setServeUserToday(0);
            }
        }
        return AjaxResult.success(digitalEmployeeList);
    }

}