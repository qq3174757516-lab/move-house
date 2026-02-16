package com.movehouse.util;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.json.JSONUtil;
import com.movehouse.common.MoveHouseException;
import com.movehouse.common.MoveHouseRemind;
import com.movehouse.common.Result;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ServletUtils {
    public static void response(HttpServletResponse res, Result<Object> result) throws IOException {
        res.setContentType("application/json;charset=utf8");
        res.getWriter().println(JSONUtil.toJsonStr(result));
    }

    public static void downloadFile(File file, String filename, HttpServletResponse res) {
        try (FileInputStream fis = new FileInputStream(file)) {
            res.setContentType("application/octet-stream;charset=utf-8");
            filename = URLEncodeUtil.encode(filename == null ? file.getName() : filename, StandardCharsets.UTF_8);
            res.setHeader("Content-disposition", "attachment;filename=" + filename);

            byte[] data = new byte[fis.available()];
            int size = fis.read(data);
            log.info("size: {}", size);
            res.getOutputStream().write(data);
            res.getOutputStream().flush();
        } catch (IOException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new MoveHouseException(MoveHouseRemind.DOWNLOAD_FAILED);
        }
    }
}
