package com.movehouse.controller;

import com.movehouse.annotation.PreAuthed;
import com.movehouse.common.MoveHouseRemind;
import com.movehouse.common.Result;
import com.movehouse.entity.Tip;
import com.movehouse.enums.UserTypeEnum;
import com.movehouse.service.TipService;
import com.movehouse.util.DbUtil;
import com.movehouse.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;

import static com.movehouse.enums.UserTypeEnum.ADMIN;

@RestController
@RequestMapping("/tip")
public class TipController {
    @Autowired
    private TipService tipService;

    /**
     * 发布一条公告信息
     *
     * @param tip 公告信息实体类
     * @return 返回操作结果
     */
    @PreAuthed
    @PostMapping
    public Result<Boolean> create(@RequestBody Tip tip) {
        tipService.save(tip.setCreateTime(new Date()));
        return Result.success(true);
    }

    /**
     * 删除一条公告信息
     *
     * @param id id
     * @return 操作结果
     */
    @PreAuthed
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        tipService.removeById(id);
        return Result.success(true);
    }

    /**
     * 修改公告信息
     *
     * @param tip 公告信息实体类
     * @return 操作结果
     */
    @PreAuthed
    @PutMapping
    public Result<Boolean> update(@RequestBody Tip tip) {
        DbUtil.checkNotFound(tip.getId(), tipService);
        tipService.updateById(tip);
        return Result.success(true);
    }

    /**
     * 查询公告信息列表
     *
     * @return 返回公告信息列表
     */
    @PreAuthed(common = true)
    @GetMapping
    public Result<List<Tip>> list() {
        UserTypeEnum userType = UserHolder.getUserType();
        List<Tip> list = tipService.lambdaQuery()
                .eq(!userType.equals(ADMIN), Tip::getStatus, 1)
                .orderByDesc(Tip::getCreateTime)
                .list();
        return Result.success(list);
    }

    /**
     * 切换公告状态 (发布/下架)
     *
     * @param id 公告ID
     */
    @PreAuthed
    @PatchMapping("/toggle/{id}")
    public Result<Boolean> toggleStatus(@PathVariable Long id) {
        Tip tip = tipService.getById(id);
        if (tip == null) {
            return Result.fail(MoveHouseRemind.TIP_NOT_FOUND);
        }
        // 对 status 进行取反操作
        // 数据库中 status 是 bit(1) 类型，对应 Integer 0/1
        // 0 表示禁用，1 表示启用
        Integer currentStatus = tip.getStatus();
        tip.setStatus(currentStatus == null || currentStatus == 0 ? 1 : 0);

        tipService.updateById(tip);
        return Result.success(true);
    }


}
