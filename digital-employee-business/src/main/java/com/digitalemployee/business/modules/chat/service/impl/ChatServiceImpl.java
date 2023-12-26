package com.digitalemployee.business.modules.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digitalemployee.business.domain.BizDigitalEmployee;
import com.digitalemployee.business.domain.BizDigitalEmployeeContext;
import com.digitalemployee.business.domain.BizUser;
import com.digitalemployee.business.mapper.BizDigitalEmployeeContextMapper;
import com.digitalemployee.business.mapper.BizDigitalEmployeeMapper;
import com.digitalemployee.business.mapper.BizUserMapper;
import com.digitalemployee.business.modules.chat.domain.ChatLoginBody;
import com.digitalemployee.business.modules.chat.domain.DigitalEmployee;
import com.digitalemployee.business.modules.chat.service.ChatService;
import com.digitalemployee.business.modules.de.domain.BizDigitalEmployeeProcedure;
import com.digitalemployee.business.modules.de.mapper.BizDigitalEmployeeProcedureMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final BizUserMapper userMapper;

    private final BizDigitalEmployeeMapper digitalEmployeeMapper;

    private final BizDigitalEmployeeContextMapper digitalEmployeeContextMapper;
    private final BizDigitalEmployeeProcedureMapper digitalEmployeeProcedureMapper;

    @Override
    public BizUser getUserInfo(Long userId) {
        LambdaQueryWrapper<BizUser> wrapper = Wrappers.lambdaQuery();
        wrapper.select(BizUser::getNickName)
                .eq(BizUser::getId, userId);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public int modifyUser(BizUser user) {

        return userMapper.updateById(user);
    }

    @Override
    public BizUser selectUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public List<DigitalEmployee> selectUserDe(Long userId) {
        BizUser bizUser = userMapper.selectById(userId);
        Long sysUserId = bizUser.getSysUserId();

        // 查询数字员工
        LambdaQueryWrapper<BizDigitalEmployee> employeeWrapper = Wrappers.lambdaQuery();
        employeeWrapper.eq(BizDigitalEmployee::getSysUserId, sysUserId);
        List<BizDigitalEmployee> digitalEmployeeList = digitalEmployeeMapper.selectList(employeeWrapper);

        if (digitalEmployeeList == null || digitalEmployeeList.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Long, BizDigitalEmployee> digitalEmployeeMap = digitalEmployeeList.stream().collect(Collectors.toMap(BizDigitalEmployee::getId, o -> o));

        // 查询数字员工Context
        LambdaQueryWrapper<BizDigitalEmployeeContext> employeeContextWrapper = Wrappers.lambdaQuery();
        employeeContextWrapper.in(BizDigitalEmployeeContext::getDigitalEmployeeId, digitalEmployeeMap.keySet());
        List<BizDigitalEmployeeContext> contextList = digitalEmployeeContextMapper.selectList(employeeContextWrapper);
        Map<Long, List<BizDigitalEmployeeContext>> contextMap = contextList.stream().collect(Collectors.groupingBy(BizDigitalEmployeeContext::getDigitalEmployeeId));

        LambdaQueryWrapper<BizDigitalEmployeeProcedure> procedureWrapper = Wrappers.lambdaQuery();
        procedureWrapper.in(BizDigitalEmployeeProcedure::getDigitalEmployeeId, digitalEmployeeMap.keySet());
        List<BizDigitalEmployeeProcedure> procedureList = digitalEmployeeProcedureMapper.selectList(procedureWrapper);
        Map<Long, List<BizDigitalEmployeeProcedure>> procedureMap = procedureList.stream().collect(Collectors.groupingBy(BizDigitalEmployeeProcedure::getDigitalEmployeeId));

        // 处理结果
        // digitalEmployeeList.forEach(de -> de.setContext(contextMap.get(de.getId())));
        return digitalEmployeeList.stream().map(de -> {
            DigitalEmployee employee = new DigitalEmployee();
            BeanUtils.copyProperties(de, employee);
            List<BizDigitalEmployeeContext> context = contextMap.get(de.getId());
            if (context == null) {
                context = new ArrayList<>();
            }
            employee.setContext(context);

            List<BizDigitalEmployeeProcedure> tempProcedureList = procedureMap.get(de.getId());
            if (tempProcedureList == null) {
                tempProcedureList = new ArrayList<>();
            }
            employee.setProcedureList(tempProcedureList);

            employee.initModelConfig();
            return employee;
        }).collect(Collectors.toList());
    }
}
