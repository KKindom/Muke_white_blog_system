package com.zsc.blog.web.AdminController;

import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.entity.TComment;
import com.zsc.blog.service.ITCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class AdminCommentController {
    @Autowired
    ITCommentService itCommentService;

    @ResponseBody
    @PostMapping("admin/comment/getList")
    public ResponseData<Object> getList(@RequestHeader("token") String token, @RequestBody Map<String, Integer> data) {
        int articleId = data.get("id");
        int commentCount = itCommentService.queryCommentWithId(articleId);
        int pageNo = data.get("pageNo");
        int pageSize = data.get("pageSize");
        int MAX_Page= commentCount/pageSize+1;
        int last= commentCount%pageSize;

        List<Map<String, Object>> page_user;
        if(MAX_Page>pageNo) {
            page_user= itCommentService.selectCommentPage(articleId,(pageNo - 1)*pageSize,pageSize,pageNo,pageSize);
        }
        else {
            page_user= itCommentService.selectCommentPage(articleId,(pageNo - 1)*pageSize,last,pageNo,pageSize);
        }
        return ResponseData.out(CodeEnum.SUCCESS, page_user);
    }

    @ResponseBody
    @PostMapping("admin/comment/getListAll")
    public ResponseData<Object> getListAll(@RequestHeader("token") String token/*, @RequestBody Map<String, Integer> data*/) {
        int commentCount = itCommentService.queryCommentNumber();
        int pageNo = 1/*data.get("pageNo")*/;
        int pageSize = 20/*data.get("pageSize")*/;
        int MAX_Page= commentCount/pageSize+1;
        int last= commentCount%pageSize;

        List<Map<String, Object>> page_user;
        if(MAX_Page>pageNo) {
            page_user= itCommentService.selectCommentPageAll((pageNo - 1)*pageSize,pageSize,pageNo,pageSize);
        }
        else {
            page_user= itCommentService.selectCommentPageAll((pageNo - 1)*pageSize,last,pageNo,pageSize);
        }
        return ResponseData.out(CodeEnum.SUCCESS, page_user);
    }

    @ResponseBody
    @PostMapping("admin/comment/delete")
    public ResponseData<Object> deleteComment(@RequestHeader("token") String token, @RequestBody Map<String, Integer> data) {
        int id = data.get("id");
        itCommentService.deleteCommentWithId(id);
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }
}
