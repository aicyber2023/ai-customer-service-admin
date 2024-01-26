package com.digitalemployee.business.modules.workbench.domain;

import com.digitalemployee.common.core.domain.entity.SysUserDeConfig;
import lombok.Data;

@Data
public class ChatResourceVO {

    private Long usedEmployeeAmount;
    private Long employeeAmount;
    private Long usedDocAmount;

    private Long docAmount;
    private Long qaFileAmount;
    private Long totalFileAmount;

    public ChatResourceVO() {
    }

    public ChatResourceVO(SysUserDeConfig sysUserDeConfig, Long usedEmployeeAmount, Long usedDocAmount, Long qaFileAmount) {
        if (sysUserDeConfig == null) {
            this.employeeAmount = 0L;
            this.docAmount = 0L;

            this.usedEmployeeAmount = 0L;
            this.usedDocAmount = 0L;
        } else {
            this.employeeAmount = sysUserDeConfig.getEmployeeAmount();
            this.docAmount = sysUserDeConfig.getKnowledgeBaseDocAmount();

            this.usedEmployeeAmount = usedEmployeeAmount;

            this.usedDocAmount = usedDocAmount;
            this.qaFileAmount = qaFileAmount;
            this.totalFileAmount = this.usedDocAmount + this.qaFileAmount;
        }
    }
}
