package com.zsc.blog.web.AdminController;

import com.sun.org.apache.bcel.internal.classfile.Code;
import com.zsc.blog.Utils.FileUploadUtils;
import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.entity.AttachFile;
import com.zsc.blog.entity.TArticle;
import com.zsc.blog.service.ITArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Controller
public class PublishArticle {
    @Autowired
    ITArticleService itArticleService;

    @Autowired
    FileUploadUtils fileUploadUtils;
    
    @ResponseBody
    @PostMapping("admin/article/newarticle")
    public ResponseData<Object> newArticle(@RequestParam(value = "file", required = false)MultipartFile file,
                                           @RequestParam("content")String content,
                                           @RequestParam("title")String title,
                                           @RequestParam("categories")String categories,
                                           @RequestHeader("token")String token) {

        //FileUploadUtils fileUploadUtils = new FileUploadUtils();
        AttachFile attachFile = new AttachFile();
        try {
            attachFile = fileUploadUtils.upload(file, 3);
        } catch (IOException e) {
            return ResponseData.out(CodeEnum.FAILURE, e.getMessage());
        }
        TArticle article = new TArticle();
        article.setCreated(new Timestamp(new Date().getTime()));
        article.setContent(content);
        article.setTitle(title);
        article.setCategories(categories);
        article.setThumbnail(attachFile.getVirtual_path());

        itArticleService.publish(article);
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }
}
