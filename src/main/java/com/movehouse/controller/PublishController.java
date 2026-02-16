package com.movehouse.controller;

import com.movehouse.annotation.PreAuthed;
import com.movehouse.common.PageParam;
import com.movehouse.common.PageResult;
import com.movehouse.common.Result;
import com.movehouse.controller.vo.PublishValuationVo;
import com.movehouse.entity.Publish;
import com.movehouse.service.PublishService;
import com.movehouse.util.DbUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import static com.movehouse.enums.UserTypeEnum.DRIVER;
import static com.movehouse.enums.UserTypeEnum.USER;

@RestController
@RequestMapping("/publish")
public class PublishController {
    @Resource
    private PublishService publishService;

    /**
     * 发布一条搬家信息
     *
     * @param publish 发布信息实体类
     * @return 返回操作结果
     */
    @PreAuthed(USER)
    @PostMapping
    public Result<Boolean> create(@RequestBody Publish publish) {
        publishService.create(publish);
        return Result.success(true);
    }

    /**
     * 根据id删除发布的搬家信息
     *
     * @param id id
     * @return 操作结果
     */
    @PreAuthed(USER)
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        publishService.removeById(id);
        return Result.success(true);
    }

    /**
     * 修改搬家信息
     *
     * @param publish 搬家信息vo
     * @return 操作结果
     */
    @PreAuthed(USER)
    @PutMapping
    public Result<Boolean> update(@RequestBody Publish publish) {
        DbUtil.checkNotFound(publish.getId(), publishService);
        publishService.updateById(publish);
        return Result.success(true);
    }

    /**
     * 查询搬家信息分页结果
     *
     * @param param 分页参数
     * @return 分页结果
     */
    @PreAuthed(common = true)
    @GetMapping("/page")
    public Result<PageResult<Publish>> page(PageParam param) {
        PageResult<Publish> result = publishService.pagePublish(param);
        return Result.success(result);
    }

    /**
     * 查询搬家信息单条数据
     *
     * @param id 数据id
     * @return 单条数据
     */
    @PreAuthed({DRIVER, USER})
    @GetMapping("/find/{id}")
    public Result<Publish> find(@PathVariable Long id) {
        Publish result = publishService.findById(id);
        return Result.success(result);
    }

    /**
     * 司机对此条搬家信息进行估价
     *
     * @param vo 估价vo
     * @return 返回操作结果
     */
    @PreAuthed(DRIVER)
    @PatchMapping("/valuation")
    public Result<Boolean> valuation(@RequestBody PublishValuationVo vo) {
        publishService.valuation(vo);
        return Result.success(true);
    }

    /**
     * 司机评估价格后, 用户是否接受
     *
     * @param id       publishId
     * @param isAccept 1同意, 0拒绝
     */
    @PreAuthed(USER)
    @PatchMapping("/process/{id}/{isAccept}")
    public Result<Boolean> process(@PathVariable Long id, @PathVariable Integer isAccept) {
        publishService.process(id, isAccept);
        return Result.success(true);
    }
}
