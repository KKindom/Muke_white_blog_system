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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseData<Object> GetAdminInfo(@RequestHeader("token") String token) {
        /*if (StringUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }*/
        DecodedJWT jwt = null;
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("jung")).build();
        jwt = verifier.verify(token);
        String dataString = jwt.getClaim("data").asString();
        String userName = JSON.parseObject(dataString).getString("username");

        Map<String,String> data=new HashMap<>();
        /*判断非admin权限，似乎没有必要
        if(permission != "admin") {
            data.put("error", "Illegal account!");
            return ResponseData.out(CodeEnum.FAILURE, data);
        }*/
        TUser user = itUserService.selectByusername(userName);
        data.put("nickname", user.getNickname());
        data.put("imgurl", user.getProfilephoto());
        data.put("permissions", user.getPermisson());

        return ResponseData.out(CodeEnum.SUCCESS, data);
    }

    @ResponseBody
    @PostMapping("admin")
    public ResponseData<Object> GetBlogInfo() {
        Map<String, Integer> data = new HashMap<>();
        data.put("user", itUserService.queryUserNumber());
        data.put("article", itArticleService.allArticle());
        data.put("comment", itCommentService.queryCommentNumber());
        return ResponseData.out(CodeEnum.SUCCESS, data);
    }
}
