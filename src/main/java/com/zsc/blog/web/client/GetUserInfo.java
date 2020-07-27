package com.zsc.blog.web.client;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.service.ITUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")

public class GetUserInfo {
    @Autowired
    ITUserService itUserService;

    @ResponseBody
    @PostMapping("/userInfo")
    public ResponseData<Map<String,String>> GetInfo() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("token");
        /*if (StringUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }*/

        DecodedJWT jwt = null;
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("jung")).build();
        jwt = verifier.verify(token);
        String dataString = jwt.getClaim("data").asString();
        String userName = JSON.parseObject(dataString).getString("username");
        String permission = JSON.parseObject(dataString).getString("permission");

        Map<String,String> data=new HashMap<>();
        TUser user = itUserService.selectByusername(userName);
        data.put("nickname", user.getNickname());
        data.put("imgurl", user.getProfilephoto());
        data.put("permissions", user.getPermisson());

        return ResponseData.out(CodeEnum.SUCCESS, data);
    }
}
