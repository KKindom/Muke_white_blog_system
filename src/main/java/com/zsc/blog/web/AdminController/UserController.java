package com.zsc.blog.web.AdminController;

import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.service.ITUserService;
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
    public ResponseData<Object> getList(@RequestHeader("token") String token, @RequestBody Map<String, String> data) {
        int userCount = itUserService.queryUserNumber();
        int pageNo = Integer.parseInt(data.get("pageNo"));
        int pageSize = Integer.parseInt(data.get("pageSize"));
        int MAX_Page= userCount/pageSize+1;
        int last= userCount%pageSize;

        List<TUser> page_user;
        if(MAX_Page>pageNo) {
            page_user= itUserService.adminSelectUser((pageNo - 1)*pageSize,pageSize,pageNo,pageSize);
        }
        else {
            page_user= itUserService.adminSelectUser((pageNo - 1)*pageSize,last,pageNo,pageSize);
        }
        return ResponseData.out(CodeEnum.SUCCESS, page_user, userCount);
    }

    //删除用户
    @ResponseBody
    @PostMapping("admin/user/delete")
    public ResponseData<Object> deleteUser(@RequestHeader("token") String token, @RequestBody Map<String, Integer> data) {
        int id = data.get("id");
        itUserService.deleteUserWithId(id);
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }
}
