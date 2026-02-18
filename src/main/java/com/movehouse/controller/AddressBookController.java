package com.movehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.movehouse.annotation.PreAuthed;
import com.movehouse.common.Result;
import com.movehouse.entity.AddressBook;
import com.movehouse.enums.UserTypeEnum;
import com.movehouse.service.AddressBookService;
import com.movehouse.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    // 获取当前用户的地址列表
    @PreAuthed
    @GetMapping("/list")
    public Result<List<AddressBook>> list() {
        Long userId = UserHolder.get().getId();
        List<AddressBook> list = addressBookService.list(
                new LambdaQueryWrapper<AddressBook>().eq(AddressBook::getUserId, userId)
        );
        return Result.success(list);
    }

    // 新增或修改地址
    @PreAuthed(UserTypeEnum.USER)
    @PostMapping
    public Result<Boolean> saveOrUpdate(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(UserHolder.get().getId());
        if (addressBook.getIsDefault()) {
            // 如果设为默认，先把其他的取消默认
            addressBookService.update(new LambdaUpdateWrapper<AddressBook>()
                    .eq(AddressBook::getUserId, addressBook.getUserId())
                    .set(AddressBook::getIsDefault, false));
        }
        addressBookService.saveOrUpdate(addressBook);
        return Result.success(true);
    }

    // 删除地址
    @PreAuthed(UserTypeEnum.USER)
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        addressBookService.removeById(id);
        return Result.success(true);
    }

    // 获取单条详情
    @PreAuthed(UserTypeEnum.USER)
    @GetMapping("/{id}")
    public Result<AddressBook> getOne(@PathVariable Long id) {
        return Result.success(addressBookService.getById(id));
    }
}