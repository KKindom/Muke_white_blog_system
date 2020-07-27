package com.zsc.blog.web.client;

import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LogOut {
    @PostMapping("user/logout")
    public ResponseData<Map<String,String>> logout(@RequestHeader("token") String token) {
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }
}
