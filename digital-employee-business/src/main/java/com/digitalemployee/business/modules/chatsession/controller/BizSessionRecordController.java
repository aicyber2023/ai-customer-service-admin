package com.digitalemployee.business.modules.chatsession.controller;

import com.digitalemployee.business.modules.chatsession.domain.BizSessionRecord;
import com.digitalemployee.business.modules.chatsession.service.IBizSessionRecordService;
import com.digitalemployee.common.core.controller.BaseController;
import com.digitalemployee.common.core.page.TableDataInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 对话详单Controller
 *
 * @author aicyber
 * @date 2023-12-27
 */
@RestController
@RequestMapping("/de/sessionRecord")
@RequiredArgsConstructor
public class BizSessionRecordController extends BaseController {

    private final IBizSessionRecordService bizSessionRecordService;

    @GetMapping("/list")
    public TableDataInfo list(BizSessionRecord bizSessionRecord) {
        startPage();
        List<BizSessionRecord> list = bizSessionRecordService.selectBizSessionRecordList(bizSessionRecord);
        return getDataTable(list);
    }

}
