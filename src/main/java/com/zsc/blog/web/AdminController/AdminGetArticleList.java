package com.zsc.blog.web.AdminController;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.mapper.TArticleMapper;
import com.zsc.blog.service.ITArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
public class AdminGetArticleList {
    @Autowired
    ITArticleService itArticleService;

    @Autowired
    TArticleMapper tArticleMapper;

    @Resource
    RedisUtil redisUtil;

    @ResponseBody
    @PostMapping("admin/article/getList")
    public ResponseData<Object>GetList(@RequestHeader("token") String token, @RequestBody Map<String, String> Body) {
        /*验证权限，备用
        DecodedJWT jwt = null;
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("jung")).build();
        jwt = verifier.verify(token);
        String dataString = jwt.getClaim("data").asString();
        String permissions = JSON.parseObject(dataString).getString("permissions");
        if(!permissions.equals("admin")) {
            return new ResponseData(CodeEnum.FAILURE_error_permisson, null);
        }*/
        int articleCount = itArticleService.allarticle();
        int pageNo = Integer.parseInt(Body.get("pageNo"));
        int pageSize = Integer.parseInt(Body.get("pageSize"));
        int MAX_Page= articleCount/pageSize+1;
        int last= articleCount%pageSize;

        List<Map<String, Object>> page_articles;
        if(MAX_Page>pageNo)
        {
            page_articles= itArticleService.admin_select_page((pageNo - 1)*pageSize,pageSize,pageNo);
        }
        else
        {
            page_articles= itArticleService.admin_select_page((pageNo - 1)*pageSize,last,pageNo);
        }
        if(page_articles.size() == 0)
        {
            return ResponseData.out(CodeEnum.FAILURE, null);
        }
        return ResponseData.out(CodeEnum.SUCCESS, page_articles, articleCount);
    }
}
