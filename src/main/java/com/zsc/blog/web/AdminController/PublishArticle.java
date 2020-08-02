package com.zsc.blog.web.AdminController;

import com.zsc.blog.Utils.FileUploadUtils;
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
import java.time.LocalDate;

@Controller
public class PublishArticle {
    @Autowired
    ITArticleService itArticleService;

    @ResponseBody
    @PostMapping("admin/article/newarticle")
    public void newArticle(@RequestParam(value = "file", required = false)MultipartFile file,
                           @RequestParam("content")String content,
                           @RequestParam("title")String title,
                           @RequestParam("categories")String categories,
                           @RequestHeader("token")String token) {

        FileUploadUtils fileUploadUtils = new FileUploadUtils();
        AttachFile attachFile = new AttachFile();
        try {
            attachFile = fileUploadUtils.upload(file, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TArticle article = new TArticle();
        article.setCreated(LocalDate.now());
        article.setContent(content);
        article.setTitle(title);
        article.setCategories(categories);
        article.setThumbnail(attachFile.getOriginalFilename());

        itArticleService.publish(article);
    }
}
