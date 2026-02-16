package com.movehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movehouse.entity.Tip;
import com.movehouse.mapper.TipMapper;
import com.movehouse.service.TipService;
import org.springframework.stereotype.Service;


@Service
public class TipServiceImpl extends ServiceImpl<TipMapper, Tip> implements TipService {

}
