package com.movehouse.controller;

import com.movehouse.annotation.PreAuthed;
import com.movehouse.common.PageResult;
import com.movehouse.common.Result;
import com.movehouse.controller.vo.ComplaintQueryVo;
import com.movehouse.entity.Complaint;
import com.movehouse.service.ComplaintService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import static com.movehouse.enums.UserTypeEnum.*;

@RestController
@RequestMapping("/complaint")
public class ComplaintController {
    @Resource
    private ComplaintService complaintService;

    /**
     * 用户添加一条投诉信息
     *
     * @param complaint 投诉信息实体类
     * @return 返回操作结果
     */
    @PreAuthed(USER)
    @PostMapping
    public Result<Boolean> create(@RequestBody Complaint complaint) {
        complaintService.create(complaint);
        return Result.success(true);
    }

    /**
     * 根据参数查询投诉信息分页
     *
     * @param vo 参数
     * @return 返回分页结果
     */
    @PreAuthed({DRIVER, ADMIN})
    @GetMapping("/page")
    public Result<PageResult<Complaint>> page(ComplaintQueryVo vo) {
        PageResult<Complaint> pageResult = complaintService.pageComplaint(vo);
        return Result.success(pageResult);
    }

    /**
     * 删除投诉信息 (视为已处理)
     */
    @PreAuthed
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        complaintService.removeById(id);
        return Result.success(true);
    }


}
