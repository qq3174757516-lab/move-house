package com.movehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.movehouse.common.PageResult;
import com.movehouse.controller.vo.ComplaintQueryVo;
import com.movehouse.entity.Complaint;


public interface ComplaintService extends IService<Complaint> {

    PageResult<Complaint> pageComplaint(ComplaintQueryVo vo);

    void create(Complaint complaint);
}
