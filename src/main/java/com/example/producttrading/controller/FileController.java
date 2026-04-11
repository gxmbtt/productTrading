package com.example.producttrading.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.example.producttrading.utils.CustomException;
import com.example.producttrading.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author guoxin
 * @date 2026年04月08日 14:29
 */
@RestController
public class FileController {
    @Value("${files.upload.path}")
    private String uploadPath;
    @Value("${files.download.path}")
    private String downloadPath;

    @PostMapping("/upload")
    public Result uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws Exception{
        //获取文件的后缀名（类型）
        String ext = FileUtil.extName(file.getOriginalFilename());

        //创建文件存储目录
        File dir = new File(uploadPath);
        if (dir.exists() == false){
            dir.mkdirs();
        }

        //生成唯一标识码（文件名）
        String uuid = UUID.randomUUID().toString();
        //保存文件
        File dest = new File(uploadPath + uuid + "." + ext);
        file.transferTo(dest);

        String url = StrUtil.removeSuffix(request.getRequestURL(),"/upload") + "/api/" + uuid + "." + ext;
        return Result.success(url);
    }
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws Exception {
        //获取文件路径
        Path path = Paths.get(downloadPath).resolve(filename).normalize();
        Resource resource = new UrlResource(path.toUri());

        if (resource.exists() && resource.isReadable()){
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""+ URLEncoder.encode(resource.getFilename(),"utf-8") +"\"")
                    .body(resource);
        }else{
            throw  new CustomException("文件不存在！");
        }
    }
}
