package com.zsc.blog.web.AdminController;

import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.service.ITUserService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class AdminManagerController {
    @Autowired
    ITUserService itUserService;

    @ResponseBody
    @PostMapping("admin/apply/getList")
    public ResponseData<Object> getRequest(@RequestHeader("token")String token, @RequestBody Map<String, String> Body) {
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(data.getKey() != "admin") {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        int pageNo = Integer.parseInt(Body.get("pageNo"));
        int pageSize = Integer.parseInt(Body.get("pageSize"));
        Pair<Integer, List<String>> result = itUserService.getRequestList(pageNo, pageSize);
        return ResponseData.out(CodeEnum.SUCCESS, result.getValue(), result.getKey());
    }

    @ResponseBody
    @PostMapping("admin/apply/accept")
    public ResponseData<Object> acceptRequest(@RequestHeader("token")String token, @RequestBody Map<String, String> Body) {
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(data.getKey() != "admin") {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        String username = Body.get("username");
        itUserService.processRequest(username, 1);
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }

    @ResponseBody
    @PostMapping("admin/apply/refuse")
    public ResponseData<Object> refuseRequest(@RequestHeader("token")String token, @RequestBody Map<String, String> Body) {
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(data.getKey() != "admin") {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        String username = Body.get("username");
        itUserService.processRequest(username, 0);
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }
}
