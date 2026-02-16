package com.movehouse.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movehouse.common.LoginUser;
import com.movehouse.common.PageParam;
import com.movehouse.common.PageResult;
import com.movehouse.controller.vo.PublishValuationVo;
import com.movehouse.entity.Publish;
import com.movehouse.enums.UserTypeEnum;
import com.movehouse.mapper.PublishMapper;
import com.movehouse.service.OrderService;
import com.movehouse.service.PublishService;
import com.movehouse.util.DbUtil;
import com.movehouse.util.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static com.movehouse.enums.UserTypeEnum.DRIVER;
import static com.movehouse.enums.UserTypeEnum.USER;


@Service
public class PublishServiceImpl extends ServiceImpl<PublishMapper, Publish> implements PublishService {
    @Resource
    private OrderService orderService;

    @Override
    public void create(Publish publish) {
        LoginUser loginUser = UserHolder.get();
        publish.setUserId(loginUser.getId())
                .setUsername(loginUser.getUsername())
                .setCreateTime(new Date());
        save(publish);
    }

    @Override
    public PageResult<Publish> pagePublish(PageParam param) {
        UserTypeEnum userType = UserHolder.getUserType();
        String keyword = param.getKeyword();
        Page<Publish> page = lambdaQuery().and(StrUtil.isNotBlank(keyword), e -> e
                        .like(Publish::getTitle, keyword).or()
                        .like(Publish::getAddress, keyword))
                //用户只能查询自己发布的信息
                .eq(userType.equals(USER), Publish::getUserId, UserHolder.getId())
                .ne(userType.equals(DRIVER), Publish::getStatus, 2)
                .orderByDesc(Publish::getCreateTime)
                .page(param.page());
        return new PageResult<>(page.getPages(), page.getRecords());
    }

    @Transactional
    @Override
    public void process(Long id, Integer isAccept) {
        Publish publish = DbUtil.checkNotFound(id, this);
        if (isAccept == 0) {
            publish.setStatus(0);
            publish.setValuation(new BigDecimal("0"));
        } else {
            publish.setStatus(2);
            orderService.payment(publish);
        }
        updateById(publish);
    }

    @Override
    public void valuation(PublishValuationVo vo) {
        Publish publish = DbUtil.checkNotFound(vo.getId(), this);
        publish.setValuation(vo.getValuation())
                .setDriverId(UserHolder.getId())
                .setStatus(1);
        updateById(publish);
    }

    @Override
    public Publish findById(Long id) {
        return DbUtil.checkNotFound(id, this);
    }
}
