package com.zsc.blog.web.UserController;

import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class LogOut {
    @ResponseBody
    @PostMapping("user/logout2")
    public ResponseData<Map<String,String>> logout(@RequestHeader("token") String token) {
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }
}
