package com.zsc.blog.web.AdminController;

import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.mapper.TArticleMapper;
import com.zsc.blog.mapper.TCommentMapper;
import com.zsc.blog.service.ITArticleService;
import com.zsc.blog.service.ITCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DeleteArticle {
    @Autowired
    ITArticleService itArticleService;

    @ResponseBody
    @PostMapping("admin/article/deletearticle")
    public ResponseData<Object> Delete(@RequestParam int id) {
        itArticleService.deleteArticleWithId(id);
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }
}
