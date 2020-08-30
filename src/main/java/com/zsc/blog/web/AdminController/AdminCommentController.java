package com.zsc.blog.web.AdminController;

import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.service.ITCommentService;
import com.zsc.blog.service.ITUserService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class AdminCommentController {
    @Autowired
    ITCommentService itCommentService;
    @Autowired
    ITUserService itUserService;

    @ResponseBody
    @PostMapping("admin/comment/getList")
    public ResponseData<Object> getList(@RequestHeader("token") String token, @RequestBody Map<String, Integer> body) {
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(!data.getKey().equals("admin") && !data.getKey().equals("root")) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        int articleId = body.get("id");
        int pageNo = body.get("pageNo");
        int pageSize = body.get("pageSize");
        if(data.getKey().equals("root") ) {
            int rootId = data.getValue();
            int commentCount = itCommentService.queryCommentWithAId(rootId, articleId);
            int MAX_Page = commentCount / pageSize + 1;
            int last = commentCount % pageSize;
            List<Map<String, Object>> page_comment;
            if (MAX_Page > pageNo) {
                page_comment = itCommentService.selectCommentPage(rootId, articleId, (pageNo - 1) * pageSize, pageSize, pageNo, pageSize);
            } else {
                page_comment = itCommentService.selectCommentPage(rootId, articleId, (pageNo - 1) * pageSize, last, pageNo, pageSize);
            }
            return ResponseData.out(CodeEnum.SUCCESS, page_comment, commentCount);

        }
        else {
            int commentCount = itCommentService.queryCommentWithAId(articleId);
            int MAX_Page = commentCount / pageSize + 1;
            int last = commentCount % pageSize;
            List<Map<String, Object>> page_comment;
            if (MAX_Page > pageNo) {
                page_comment = itCommentService.selectCommentPage(articleId, (pageNo - 1) * pageSize, pageSize, pageNo, pageSize);
            } else {
                page_comment = itCommentService.selectCommentPage(articleId, (pageNo - 1) * pageSize, last, pageNo, pageSize);
            }
            return ResponseData.out(CodeEnum.SUCCESS, page_comment, commentCount);
        }
    }

    @ResponseBody
    @PostMapping("admin/comment/getListAll")
    public ResponseData<Object> getListAll(@RequestHeader("token") String token, @RequestBody Map<String, Integer> body) {
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(!data.getKey().equals("admin") && !data.getKey().equals("root")) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        int pageNo = body.get("pageNo");
        int pageSize = body.get("pageSize");

        if(data.getKey().equals("root") ) {
            int rootId = data.getValue();
            int commentCount = itCommentService.queryCommentNumber(rootId);
            int MAX_Page = commentCount / pageSize + 1;
            int last = commentCount % pageSize;

            List<Map<String, Object>> page_comment;
            if (MAX_Page > pageNo) {
                page_comment = itCommentService.selectCommentPageAll(rootId, (pageNo - 1) * pageSize, pageSize, pageNo, pageSize);
            } else {
                page_comment = itCommentService.selectCommentPageAll(rootId, (pageNo - 1) * pageSize, last, pageNo, pageSize);
            }
            return ResponseData.out(CodeEnum.SUCCESS, page_comment, commentCount);
        }
        else {
            int commentCount = itCommentService.queryCommentNumber();
            int MAX_Page = commentCount / pageSize + 1;
            int last = commentCount % pageSize;

            List<Map<String, Object>> page_comment;
            if (MAX_Page > pageNo) {
                page_comment = itCommentService.selectCommentPageAll((pageNo - 1) * pageSize, pageSize, pageNo, pageSize);
            } else {
                page_comment = itCommentService.selectCommentPageAll((pageNo - 1) * pageSize, last, pageNo, pageSize);
            }
            return ResponseData.out(CodeEnum.SUCCESS, page_comment, commentCount);
        }
    }

    @ResponseBody
    @PostMapping("admin/comment/delete")
    public ResponseData<Object> deleteComment(@RequestHeader("token") String token, @RequestBody Map<String, Integer> body) {
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(!data.getKey().equals("admin") && !data.getKey().equals("root")) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        int id = body.get("id");
        itCommentService.deleteCommentWithId(id);
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }
}
