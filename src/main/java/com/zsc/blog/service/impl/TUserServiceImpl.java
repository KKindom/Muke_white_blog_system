package com.zsc.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsc.blog.Utils.MailUtils;
import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.mapper.TCollectMapper;
import com.zsc.blog.mapper.TCommentMapper;
import com.zsc.blog.mapper.TStatisticMapper;
import com.zsc.blog.mapper.TUserMapper;
import com.zsc.blog.service.ITUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mff
 * @since 2020-07-26
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements ITUserService {
    @Autowired
    TUserMapper tUserMapper;
    @Autowired
    TCommentMapper tCommentMapper;
    @Autowired
    TCollectMapper tCollectMapper;
    @Autowired
    TStatisticMapper tStatisticMapper;
    @Autowired
    MailUtils mailUtils;
    @Resource
    RedisUtil redisUtil;

    //查询所有用户
    @Override
    public Collection<?> selectList(Object o) {
        return tUserMapper.selectList(null);
    }
    //根据名字查询
    @Override
    public TUser selectByusername(String username) {
//        QueryWrapper<TUser> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(TUser::getUsername,username);
        return tUserMapper.selectbyname(username);
    }
    //根据id查询
    @Override
    public TUser selectById(String userId) {
        return tUserMapper.selectById(userId);
    }
    //插入用户
    @Override
    public void insert_user(TUser tUser)
    {
        redisUtil.set(tUser.getUsername(),tUser);
        tUserMapper.insert(tUser);
    }

    @Override
    public void updata_I(TUser tUser) {
        tUserMapper.updateById(tUser);
    }

    @Override
    public int find_usercount(String username) {
      int count=  tUserMapper.selectCount(new QueryWrapper<TUser>().eq("username", username));
        return count;
    }

    @Override
    public int queryUserNumber(){
        System.out.println(tUserMapper.queryCount());
        return tUserMapper.queryCount();
    }

    @Override
    public int queryUserNumber(int rootId){
        return tUserMapper.queryCountByRoot(rootId);
    }

    @Override
    public void deleteUserWithId(int id) {
        TUser tUser = tUserMapper.selectById(id);
        String username = tUser.getUsername();
        redisUtil.del(username);
        redisUtil.removeAll("comment");
        redisUtil.removeAll("userPage_");
        tUserMapper.deleteUser(id);
        tCollectMapper.deleteColletWithUid(id);
        tCommentMapper.deleteCommentWithUser(username);
        tStatisticMapper.updateStatisticComment();
    }

    @Override
    public List<TUser> adminSelectUser(int st, int en, int num, int pageSize) {
        List<TUser> resultList;
        if (redisUtil.get("adminUserPage_"+num+"pageSize_"+pageSize)==null) {
            resultList=tUserMapper.selectUser(st, en);
            redisUtil.set("adminUserPage_"+num+"pageSize_"+pageSize,resultList, 30);
        }
        else {
            resultList =(List<TUser>)redisUtil.get("adminUserPage_"+num+"pageSize_"+pageSize);
        }
        return resultList;
    }

    @Override
    public List<TUser> adminSelectUser(int rootId, int st, int en, int num, int pageSize) {
        List<TUser> resultList;
        if (redisUtil.get("root"+rootId+"userPage_"+num+"pageSize_"+pageSize)==null) {
            resultList=tUserMapper.selectUserByRoot(rootId, st, en);
            redisUtil.set("root"+rootId+"userPage_"+num+"pageSize_"+pageSize,resultList, 30);
        }
        else {
            resultList =(List<TUser>)redisUtil.get("root"+rootId+"userPage_"+num+"pageSize_"+pageSize);
        }
        return resultList;
    }

    @Override
    public Pair<String, Integer> checkPermisson(String token) {
        DecodedJWT jwt = null;
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("jung")).build();
        jwt = verifier.verify(token);
        String dataString = jwt.getClaim("data").asString();
        String permissions = JSON.parseObject(dataString).getString("permissions");
        int id = Integer.parseInt(JSON.parseObject(dataString).getString("id"));
        return new Pair<String, Integer>(permissions, id);
    }

    @Override
    public Pair<Integer, List<String>> getRequestList(int pageNo, int pageSize) {
        List<String> list = new ArrayList<String>();
        if(redisUtil.get("requestList") == null) {
            Set<String> data = redisUtil.keys("Apply_Author_");
            Iterator<String> it = data.iterator();
            while (it.hasNext()) {
                String str = it.next();
                list.add(str);
            }
            redisUtil.set("requestList", list, 30);
        }
        else {
            list = (List<String>)redisUtil.get("requestList");
        }
        int requestCount = list.size();
        int MAX_Page= requestCount/pageSize+1;
        int last= list.size();
        List<String> resultList = new ArrayList<String>();
        if(MAX_Page > pageNo) {
            resultList = list.subList((pageNo - 1)*pageSize, pageNo*pageSize);
        }
        else {
            resultList = list.subList((pageNo - 1)*pageSize, requestCount);
        }
        return new Pair<Integer, List<String>>(requestCount, resultList);
    }

    @Override
    public void processRequest(String username, int type) {
        redisUtil.del("Apply_Author_" + username);
        TUser tUser = tUserMapper.selectbyname(username);
        String email = tUser.getEmail();
        if(type == 1) {
            tUserMapper.updateUserPermisson(username);
            String content = "恭喜，你的申请已经通过，你已经成为了本站的管理员。";
            mailUtils.sendApplyResultEmail(email, content);
        }
        else {
            String content = "对不起，你的申请没有通过。";
            mailUtils.sendApplyResultEmail(email, content);
        }
    }
}
