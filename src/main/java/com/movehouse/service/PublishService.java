package com.movehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.movehouse.common.PageParam;
import com.movehouse.common.PageResult;
import com.movehouse.controller.vo.PublishValuationVo;
import com.movehouse.entity.Publish;


public interface PublishService extends IService<Publish> {

    void create(Publish publish);

    PageResult<Publish> pagePublish(PageParam param);

    void process(Long id, Integer isAccept);

    void valuation(PublishValuationVo vo);

    Publish findById(Long id);
}
