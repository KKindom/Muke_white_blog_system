package com.zsc.blog.web.AdminController;

import com.zsc.blog.Utils.FileUploadUtils;
import com.zsc.blog.Utils.SensitiveFilterService;
import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.entity.*;
import com.zsc.blog.mapper.TArticleMapper;
import com.zsc.blog.service.ITArticleService;
import com.zsc.blog.service.ITUserService;
import com.zsc.blog.service.impl.TUserServiceImpl;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class AdminArticleController {
    @Autowired
    ITArticleService itArticleService;
    @Autowired
    FileUploadUtils fileUploadUtils;
    @Autowired
    TArticleMapper tArticleMapper;
    @Autowired
    ITUserService itUserService;

    //展示文章列表
    @ResponseBody
    @PostMapping("admin/article/getList")
    public ResponseData<Object> GetList(@RequestHeader("token") String token, @RequestBody Map<String, String> Body) {
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(!data.getKey().equals("admin") && !data.getKey().equals("root")) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        int pageNo = Integer.parseInt(Body.get("pageNo"));
        int pageSize = Integer.parseInt(Body.get("pageSize"));
        if(data.getKey().equals("root") ) {
            int rootId = data.getValue();
            int articleCount = itArticleService.allArticle(rootId);
            int MAX_Page = articleCount / pageSize + 1;
            int last = articleCount % pageSize;

            List<Map<String, Object>> page_articles;
            if (MAX_Page > pageNo) {
                page_articles = itArticleService.adminSelectPage(rootId, (pageNo - 1) * pageSize, pageSize, pageNo, pageSize);
            } else {
                page_articles = itArticleService.adminSelectPage(rootId, (pageNo - 1) * pageSize, last, pageNo, pageSize);
            }
            return ResponseData.out(CodeEnum.SUCCESS, page_articles, articleCount);
        }
        else {
            int articleCount = itArticleService.allArticle();
            int MAX_Page = articleCount / pageSize + 1;
            int last = articleCount % pageSize;

            List<Map<String, Object>> page_articles;
            if (MAX_Page > pageNo) {
                page_articles = itArticleService.adminSelectPage((pageNo - 1) * pageSize, pageSize, pageNo, pageSize);
            } else {
                page_articles = itArticleService.adminSelectPage((pageNo - 1) * pageSize, last, pageNo, pageSize);
            }
            return ResponseData.out(CodeEnum.SUCCESS, page_articles, articleCount);
        }
    }

    //根据ID删除文章
    @ResponseBody
    @PostMapping("admin/article/deletearticle")
    public ResponseData<Object> Delete(@RequestHeader("token") String token, @RequestParam int id) {
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(!data.getKey().equals("admin") && !data.getKey().equals("root")) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        TArticle tArticle = itArticleService.selectArticleWithId(id);
        TUser articleAuthor = itUserService.selectByusername(tArticle.getAuthor());
        if(articleAuthor.getPermisson().equals("admin")  && data.getKey().equals("root") ) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        if(data.getKey().equals("root")  && articleAuthor.getId() != data.getValue()) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, "这不是你发布的文章");
        }
        itArticleService.deleteArticleWithId(id);
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }

    //发布文章
    @ResponseBody
    @PostMapping("admin/article/newarticle")
    public ResponseData<Object> newArticle(@RequestParam(value = "file", required = false) MultipartFile file,
                                           @RequestParam("content")String content,
                                           @RequestParam("title")String title,
                                           @RequestParam("categories")String categories,
                                           @RequestHeader("token")String token) {

        //FileUploadUtils fileUploadUtils = new FileUploadUtils();
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(!data.getKey().equals("admin") && !data.getKey().equals("root")) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        //初始化过滤器
        SensitiveFilterService filter = SensitiveFilterService.getInstance();
        //过滤文字
        String hou = filter.replaceSensitiveWord(content, 1, "*");
        //判定是否过滤成功  true为过滤失败 有敏感字 false为过滤成功 可通过
        if(filter.end()) {
            return ResponseData.out(CodeEnum.FAILURE_Sensitive, null);
        }
        AttachFile attachFile = new AttachFile();
        try {
            attachFile = fileUploadUtils.upload(file, 3);
        } catch (IOException e) {
            return ResponseData.out(CodeEnum.FAILURE, e.getMessage());
        }
        TArticle article = new TArticle();
        article.setCreated(new Timestamp(new Date().getTime()));
        article.setAuthor(itUserService.selectById(data.getValue().toString()).getUsername());
        article.setContent(content);
        article.setTitle(title);
        article.setCategories(categories);
        article.setThumbnail(attachFile.getVirtual_path());

        itArticleService.publish(article);
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }

    //修改文章
    @ResponseBody
    @PostMapping("admin/article/modify")
    public ResponseData<Object> modifyArticle(@RequestParam(value = "file", required = false) MultipartFile file,
                         @RequestParam("id")int id,
                         @RequestParam(value = "content", required = false)String content,
                         @RequestParam(value = "title", required = false)String title,
                         @RequestParam(value = "categories", required = false)String categories,
                         @RequestHeader("token")String token) {
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(!data.getKey().equals("admin") && !data.getKey().equals("root")) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        TArticle tArticle = itArticleService.selectArticleWithId(id);
        TUser articleAuthor = itUserService.selectByusername(tArticle.getAuthor());
        if(data.getKey().equals("root")  && articleAuthor.getId() != data.getValue()) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, "这不是你的文章！");
        }
        tArticle.setModified(new Timestamp(new Date().getTime()));
        if(file != null) {
            AttachFile attachFile = new AttachFile();
            try {
                attachFile = fileUploadUtils.upload(file, 3);
            } catch (IOException e) {
                return ResponseData.out(CodeEnum.FAILURE, e.getMessage());
            }
            tArticle.setThumbnail(attachFile.getVirtual_path());
        }
        if(content != null) {
            tArticle.setContent(content);
        }
        if(title != null) {
            tArticle.setTitle(title);
        }
        if(categories != null) {
            tArticle.setCategories(categories);
        }
        itArticleService.updateArticle(tArticle);
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }


    //点击显示文字内容以及评论
    @ResponseBody
    @RequestMapping(value = "admin/article/showarticle",method = RequestMethod.POST)
    public ResponseData<TArticle> Show_article(@RequestBody Map<String,String>  requestdata) {
        String id=requestdata.get("id");
        TArticle tArticle=tArticleMapper.selectArticleWithId(Integer.parseInt(id) );
        return ResponseData.out(CodeEnum.SUCCESS,tArticle);
    }
}
