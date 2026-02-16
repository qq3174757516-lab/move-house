package com.movehouse.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movehouse.common.LoginUser;
import com.movehouse.common.MoveHouseRemind;
import com.movehouse.common.PageParam;
import com.movehouse.common.PageResult;
import com.movehouse.controller.vo.LoginVo;
import com.movehouse.controller.vo.RegisterVo;
import com.movehouse.entity.User;
import com.movehouse.mapper.UserMapper;
import com.movehouse.service.UserService;
import com.movehouse.util.TokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static cn.hutool.crypto.SecureUtil.md5;
import static com.movehouse.common.MoveHouseException.exception;
import static com.movehouse.common.MoveHouseRemind.USERNAME_OR_PASSWORD_ERROR;
import static com.movehouse.common.MoveHouseRemind.USER_EXISTS;
import static com.movehouse.enums.UserTypeEnum.USER;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public LoginUser login(LoginVo vo) {
        String username = vo.getUsername();
        User user = findByUsername(username);
        if (user == null) {
            user = findByPhone(username);
        }

        //校验用户名
        if (user == null) {
            throw exception(USERNAME_OR_PASSWORD_ERROR);
        }

        //校验密码
        String password = md5(vo.getPassword());
        if (!StrUtil.equals(password, user.getPassword())) {
            throw exception(USERNAME_OR_PASSWORD_ERROR);
        }

        //生成token并返回
        LoginUser loginUser = new LoginUser(user.getId(), USER.getCode(), user.getUsername(), null);
        TokenUtil.createToken(loginUser);
        return loginUser;
    }

    @Transactional
    @Override
    public void register(RegisterVo vo) {
        //校验用户名是否已经存在
        User user = findByUsername(vo.getUsername());
        if (user != null) {
            throw exception(USER_EXISTS);
        }

        //校验手机号是否已经存在
        user = findByPhone(vo.getPhone());
        if (user != null) {
            throw exception(USER_EXISTS);
        }

        //保存用户
        vo.setPassword(md5(vo.getPassword()));
        user = BeanUtil.copyProperties(vo, User.class).setCreateTime(new Date());
        save(user);
    }

    @Override
    public void edit(User user) {
        if (StrUtil.isNotBlank(user.getPassword())) {
            user.setPassword(md5(user.getPassword()));
        }
        updateById(user);
    }

    @Override
    public PageResult<User> pageUser(PageParam queryVo) {
        String keyword = queryVo.getKeyword();
        Page<User> page = lambdaQuery()
                .like(StrUtil.isNotBlank(keyword), User::getNickname, keyword)
                .page(queryVo.page());
        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    //根据用户名查找用户
    public User findByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).one();
    }

    //根据手机查找用户
    public User findByPhone(String phone) {
        return lambdaQuery().eq(User::getPhone, phone).one();
    }

    @Override
    public void recharge(Long id, BigDecimal amount) {
        User user = getById(id);
        if (user == null) {
            throw com.movehouse.common.MoveHouseException.exception(MoveHouseRemind.USER_NOT_FOUND);
        }
        // 原余额 + 充值金额
        user.setBalance(user.getBalance().add(amount));
        updateById(user);
    }

}
