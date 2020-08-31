package com.zsc.blog.web.AdminController;

import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.service.ITUserService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    ITUserService itUserService;

    //查看用户列表
    @ResponseBody
    @PostMapping("admin/user/getList")
    public ResponseData<Object> getList(@RequestHeader("token") String token, @RequestBody Map<String, String> body) {
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(!data.getKey().equals("admin")) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }

        int pageNo = Integer.parseInt(body.get("pageNo"));
        int pageSize = Integer.parseInt(body.get("pageSize"));

        if(data.getKey().equals("root")) {
            int rootId = data.getValue();
            int userCount = itUserService.queryUserNumber(rootId);
            int MAX_Page = userCount / pageSize + 1;
            int last = userCount % pageSize;

            List<TUser> page_user;
            if (MAX_Page > pageNo) {
                page_user = itUserService.adminSelectUser(rootId,(pageNo - 1) * pageSize, pageSize, pageNo, pageSize);
            } else {
                page_user = itUserService.adminSelectUser(rootId,(pageNo - 1) * pageSize, last, pageNo, pageSize);
            }
            return ResponseData.out(CodeEnum.SUCCESS, page_user, userCount);
        }
        else {
            int userCount = itUserService.queryUserNumber();
            int MAX_Page = userCount / pageSize + 1;
            int last = userCount % pageSize;

            List<TUser> page_user;
            if (MAX_Page > pageNo) {
                page_user = itUserService.adminSelectUser((pageNo - 1) * pageSize, pageSize, pageNo, pageSize);
            } else {
                page_user = itUserService.adminSelectUser((pageNo - 1) * pageSize, last, pageNo, pageSize);
            }
            return ResponseData.out(CodeEnum.SUCCESS, page_user, userCount);
        }
    }

    //删除用户
    @ResponseBody
    @PostMapping("admin/user/delete")
    public ResponseData<Object> deleteUser(@RequestHeader("token") String token, @RequestBody Map<String, Integer> body) {
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(!data.getKey().equals("admin")) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        int id = body.get("id");
        TUser tUser = itUserService.selectById(Integer.toString(id));
        if(tUser.getPermisson().equals("admin")  || (data.getKey().equals("root")  && tUser.getPermisson().equals("root") )) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        itUserService.deleteUserWithId(id);
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }

    //关小黑屋
    @ResponseBody
    @PostMapping("admin/user/block")
    public ResponseData<Object> blockingUser(@RequestHeader("token")String token, @RequestBody Map<String, Integer> body){
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(!data.getKey().equals("admin")) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        int id = body.get("id");
        itUserService.blockUserWithId(id);
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }

    //移出小黑屋
    @ResponseBody
    @PostMapping("admin/user/unblock")
    public ResponseData<Object> unBlockingUser(@RequestHeader("token")String token, @RequestBody Map<String, Integer> body){
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(!data.getKey().equals("admin")) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        int id = body.get("id");
        itUserService.unBlockUserWithId(id);
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }

    //移除创作者权限
    @ResponseBody
    @PostMapping("admin/user/removeRoot")
    public ResponseData<Object> removeRootPermisson(@RequestHeader("token")String token, @RequestBody Map<String, Integer> body) {
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(!data.getKey().equals("admin")) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        int id = body.get("id");
        itUserService.removeRootPermisson(id);
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }
}
