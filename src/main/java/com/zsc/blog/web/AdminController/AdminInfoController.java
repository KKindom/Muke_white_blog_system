package com.zsc.blog.web.AdminController;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.service.ITArticleService;
import com.zsc.blog.service.ITCommentService;
import com.zsc.blog.service.ITUserService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AdminInfoController {
    @Autowired
    ITUserService itUserService;
    @Autowired
    ITCommentService itCommentService;
    @Autowired
    ITArticleService itArticleService;
    @Autowired
    RedisUtil redisUtil;

    @ResponseBody
    @PostMapping("admin/userInfo")
    public ResponseData<Object> GetAdminInfo(@RequestHeader("token")String token) {
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(!data.getKey().equals("admin") && !data.getKey().equals("root")) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        DecodedJWT jwt = null;
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("jung")).build();
        jwt = verifier.verify(token);
        String dataString = jwt.getClaim("data").asString();
        String userName = JSON.parseObject(dataString).getString("username");

        Map<String,String> result=new HashMap<>();
        TUser user = itUserService.selectByusername(userName);
        result.put("nickname", user.getNickname());
        result.put("imgurl", user.getProfilephoto());
        result.put("permissions", user.getPermisson());

        return ResponseData.out(CodeEnum.SUCCESS, result);
    }

    @ResponseBody
    @PostMapping("admin")
    public ResponseData<Object> GetBlogInfo(@RequestHeader("token") String token) {
        Pair<String, Integer> data = itUserService.checkPermisson(token);
        if(!data.getKey().equals("admin") && !data.getKey().equals("root")) {
            return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        }
        //if(data.getKey().equals("admin") ) {
            Map<String, Integer> result = new HashMap<>();
            result.put("user", itUserService.queryUserNumber());
            result.put("article", itArticleService.allArticle());
            result.put("comment", itCommentService.queryCommentNumber());
            return ResponseData.out(CodeEnum.SUCCESS, result);
       // }
        //else{
        //    return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
        //}
    }
}
