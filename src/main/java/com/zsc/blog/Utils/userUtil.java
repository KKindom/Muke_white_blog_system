package com.zsc.blog.Utils;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zsc.blog.entity.TUser;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class userUtil {
    private String secret = "jung";
    public String getToken(TUser user) {
        System.out.println(user);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token="";
        String audience  = user.getId()+"";
        //把用户数据传到data里面 同时permission是判定用户权限的标准
        token= JWT.create()
                .withIssuer("jung")   //发布者
                .withSubject("test")    //主题
                .withAudience(audience)     //观众，相当于接受者
                .withIssuedAt(new Date())   // 生成签名的时间
                .withClaim("data", JSON.toJSONString(user)) //存数据
                .withNotBefore(new Date())  //生效时间
                .sign(algorithm);
        return token;
    }
}
