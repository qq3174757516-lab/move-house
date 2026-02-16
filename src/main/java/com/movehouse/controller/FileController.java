package com.movehouse.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.movehouse.annotation.PreAuthed;
import com.movehouse.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author ygq
 * @date 2023-04-10 15:00
 **/
@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${file.upload-path}")
    private String uploadPath;

    /**
     * 通用文件上传接口
     *
     * @param file 文件
     * @return 返回相对路径
     * @throws IOException 异常
     */
    @PreAuthed(common = true)
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        String datePath = DateUtil.format(new Date(), "/yyyy/MM/dd/");
        filename = UUID.fastUUID().toString(true) + suffix;

        //保存文件
        File dir = new File(uploadPath + datePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file.transferTo(new File(dir, filename));

        //返回url
        return Result.success("/file" + datePath + filename);
    }
}
