package com.digitalemployee.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalemployee.business.domain.BizDigitalEmployee;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 客户数字员工Service接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
public interface IBizDigitalEmployeeService extends IService<BizDigitalEmployee> {
    /**
     * 查询客户数字员工
     *
     * @param id 客户数字员工主键
     * @return 客户数字员工
     */
    BizDigitalEmployee selectBizDigitalEmployeeById(Long id);

    /**
     * 查询客户数字员工列表
     *
     * @param bizDigitalEmployee 客户数字员工
     * @return 客户数字员工集合
     */
    List<BizDigitalEmployee> selectBizDigitalEmployeeList(BizDigitalEmployee bizDigitalEmployee);

    /**
     * 新增客户数字员工
     *
     * @param bizDigitalEmployee 客户数字员工
     * @return 结果
     */
    BizDigitalEmployee insertBizDigitalEmployee(BizDigitalEmployee bizDigitalEmployee);

    /**
     * 修改客户数字员工
     *
     * @param bizDigitalEmployee 客户数字员工
     * @return 结果
     */
    int updateBizDigitalEmployee(BizDigitalEmployee bizDigitalEmployee);

    /**
     * 批量删除客户数字员工
     *
     * @param ids 需要删除的客户数字员工主键集合
     * @return 结果
     */
    int deleteBizDigitalEmployeeByIds(Long[] ids);

    int deleteBizDigitalEmployeeByIdsLogic(Long[] ids);

    /**
     * 删除客户数字员工信息
     *
     * @param id 客户数字员工主键
     * @return 结果
     */
    int deleteBizDigitalEmployeeById(Long id);

    void saveAvatar(MultipartFile file, String employeeId);

    void findDigiEmpData(List<BizDigitalEmployee> list);

    void saveCompanyAvatar(MultipartFile file, String employeeId);
}
