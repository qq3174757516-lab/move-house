package com.movehouse.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movehouse.common.PageResult;
import com.movehouse.controller.vo.ComplaintQueryVo;
import com.movehouse.entity.Complaint;
import com.movehouse.entity.Driver;
import com.movehouse.enums.UserTypeEnum;
import com.movehouse.mapper.ComplaintMapper;
import com.movehouse.service.ComplaintService;
import com.movehouse.service.DriverService;
import com.movehouse.util.DbUtil;
import com.movehouse.util.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.movehouse.enums.UserTypeEnum.USER;


@Service
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, Complaint> implements ComplaintService {
    @Resource
    private DriverService driverService;

    @Override
    public PageResult<Complaint> pageComplaint(ComplaintQueryVo vo) {
        //根据条件查询
        UserTypeEnum userType = UserHolder.getUserType();
        Long driverId = vo.getDriverId();
        Page<Complaint> page = lambdaQuery()
                .eq(driverId != null, Complaint::getDriverId, driverId)
                //用户只能查询自己的投诉信, 管理员可以查看所有的投诉信
                .eq(userType.equals(USER), Complaint::getUserId, UserHolder.getId())
                .page(vo.page());
        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    @Transactional
    @Override
    public void create(Complaint complaint) {
        complaint.setUserId(UserHolder.getId());
        //司机投诉次数加1
        Driver driver = DbUtil.checkNotFound(complaint.getDriverId(), driverService);
        driver.setComplaintNum(driver.getComplaintNum() + 1);
        driverService.updateById(driver);
        save(complaint.setCreateTime(new Date()));
    }
}
