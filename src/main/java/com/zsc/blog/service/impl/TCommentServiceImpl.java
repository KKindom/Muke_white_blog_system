package com.zsc.blog.service.impl;

import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.entity.TComment;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.mapper.TCommentMapper;
import com.zsc.blog.mapper.TStatisticMapper;
import com.zsc.blog.mapper.TUserMapper;
import com.zsc.blog.service.ITCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mff
 * @since 2020-07-26
 */
@Service
public class TCommentServiceImpl extends ServiceImpl<TCommentMapper, TComment> implements ITCommentService {
    @Autowired
    TCommentMapper tCommentMapper;
    @Autowired
    TStatisticMapper tStatisticMapper;
    @Autowired
    TUserMapper tUserMapper;
    @Resource
    RedisUtil redisUtil;

    @Override
    public Object selectList(Object o) {
        return tCommentMapper.selectList(null);
    }

    @Override
    public List<Map<String ,String >> SelectByArticle_id(int id) {
        List<Map<String ,String >> list;
        if(redisUtil.get("commentlistby_id_"+id)==null)
        {
            list=tCommentMapper.selectcomlistby_a_id(id);
            redisUtil.set("commentlistby_id_"+id,list,60);
            System.out.println("commentlistby_id_"+id+"加入缓存成功！");
        }
        else
        {
            list=(List<Map<String ,String >>)redisUtil.get("commentlistby_id_"+id);
            System.out.println("commentlistby_id_"+id+"从缓存读出！");
        }
        return list;
    }

    @Override
    public void insertcomment(TComment tComment)
    {
        String username=tComment.getAuthor();
        if(redisUtil.get("commentlist_"+username)==null) {
            tCommentMapper.insert(tComment);
        }
        else
        {
            redisUtil.del("commentlist_"+username);
            tCommentMapper.insert(tComment);
        }
        redisUtil.del("commentlistby_id_"+tComment.getArticleId());
        System.out.println("插入评论成功！");
    }

    @Override
    public List<Map<String,String>> Selectbyusername(String username) {
        List<Map<String,String>> CommentList;
        if (redisUtil.get(  "commentlist_"+username) == null)
        {
            CommentList = tCommentMapper.selcetcommentbyusername(username);
            redisUtil.set("commentlist_"+username,CommentList,60);

        }
        else
        {
            CommentList=(List<Map<String,String>>) redisUtil.get("commentlist_"+username);
        }
        return CommentList;
    }

    @Override
    public void Delcomment(int id, String username)
    {
        if(redisUtil.get("commentlist_"+username)==null)
        {
            tCommentMapper.deleteCommentWithauthor(id, username);
        }
        else
        {
            //删除原有缓存
            redisUtil.del("commentlist_"+username);
            tCommentMapper.deleteCommentWithauthor(id, username);
        }
    }

    @Override
    public int queryCommentNumber() {
        return tCommentMapper.selectCount(null);
    }

    @Override
    public int queryCommentNumber(int rootId) {
        return tCommentMapper.querytCount(rootId);
    }

    @Override
    public int queryCommentWithAId(int id) {
        return tCommentMapper.queryCountWithAId(id);
    }

    @Override
    public int queryCommentWithAId(int rootId, int id) {
        return tCommentMapper.queryCountWithAIdByRoot(rootId, id);
    }

    @Override
    public void deleteCommentWithId(int id) {
        redisUtil.removeAll("comment");
        TComment tComment = tCommentMapper.selectById(id);
        tStatisticMapper.updateStatisticAfterDeleteComment(tComment.getArticleId());
        tCommentMapper.deleteCommentWithId(id);
    }

    @Override
    public List<Map<String, Object>> selectCommentPage(int id, int st, int en, int num, int pageSize) {
        List<Map<String, Object>> resultList;
        if (redisUtil.get("adminarticle_"+id+"commentPage_"+num+"pageSize_"+pageSize)==null) {
            resultList=tCommentMapper.selectCommentPage(id, st, en);
            redisUtil.set("adminarticle_"+id+"commentPage_"+num+"pageSize_"+pageSize,resultList,30);
        }
        else {
            resultList =(List<Map<String, Object>>)redisUtil.get("adminarticle_"+id+"commentPage_"+num+"pageSize_"+pageSize);
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> selectCommentPage(int rootId, int id, int st, int en, int num, int pageSize) {
        List<Map<String, Object>> resultList;
        if (redisUtil.get("root"+rootId+"article_"+id+"commentPage_"+num+"pageSize_"+pageSize)==null) {
            resultList=tCommentMapper.selectCommentPageByRoot(rootId, id, st, en);
            redisUtil.set("root"+rootId+"article_"+id+"commentPage_"+num+"pageSize_"+pageSize,resultList,30);
        }
        else {
            resultList =(List<Map<String, Object>>)redisUtil.get("root"+rootId+"article_"+id+"commentPage_"+num+"pageSize_"+pageSize);
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> selectCommentPageAll(int st, int en, int num, int pageSize) {
        List<Map<String, Object>> resultList;
        if (redisUtil.get("admincommentPage_"+num+"pageSize_"+pageSize)==null) {
            resultList=tCommentMapper.selectCommentPageAll(st, en);
            redisUtil.set("admincommentPage_"+num+"pageSize_"+pageSize,resultList,30);
        }
        else {
            resultList =(List<Map<String, Object>>)redisUtil.get("admincommentPage_"+num+"pageSize_"+pageSize);
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> selectCommentPageAll(int rootId, int st, int en, int num, int pageSize) {
        List<Map<String, Object>> resultList;
        if (redisUtil.get("root"+rootId+"commentPage_"+num+"pageSize_"+pageSize)==null) {
            resultList=tCommentMapper.selectCommentPageAllByRoot(rootId, st, en);
            redisUtil.set("root"+rootId+"commentPage_"+num+"pageSize_"+pageSize,resultList,30);
        }
        else {
            resultList =(List<Map<String, Object>>)redisUtil.get("root"+rootId+"commentPage_"+num+"pageSize_"+pageSize);
        }
        return resultList;
    }

    @Override
    public Map<String, Object> selectNewComment() {
        if(redisUtil.get("adminNewComment") == null) {
            List<Map<String, Object>> list = tCommentMapper.Selectcommentby_admin();
            Map<String, Object> queryResult = new HashMap<>();
            Iterator<Map<String, Object>> it = list.iterator();
            System.out.println(list);
            while (it.hasNext()) {
                Map<String, Object> now = it.next();
                queryResult.put((String)now.get("date"), (long)now.get("num"));
            }
            Calendar calendar = Calendar.getInstance();
            Map<String, Object> result = new HashMap<>();
            for (int i = 0; i < 7; i++) {
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - (i == 0 ? 0 : 1));
                Date today = calendar.getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String str = format.format(today);
                if (queryResult.get(str) == null) {
                    result.put(str, 0);
                } else {
                    result.put(str, queryResult.get(str));
                }
            }
            //将result按时间降序排序
            LinkedHashMap<String, Object> ascLinkedHashMap = result.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> newValue,
                            LinkedHashMap::new));
            redisUtil.set("adminNewComment", ascLinkedHashMap, 60);
            return ascLinkedHashMap;
        }
        else {
            Map<String, Object> result = (Map<String, Object>)redisUtil.get("adminNewComment");
            return result;
        }
    }

    @Override
    public Map<String, Object> selectNewComment(int rootId) {
        TUser tUser = tUserMapper.selectById(rootId);
        if(redisUtil.get("root"+rootId+"NewComment") == null) {
            List<Map<String, Object>> list = tCommentMapper.Selectcommentby_root(tUser.getUsername());
            Map<String, Object> queryResult = new HashMap<>();
            Iterator<Map<String, Object>> it = list.iterator();
            while (it.hasNext()) {
                Map<String, Object> now = it.next();
                queryResult.put((String)now.get("date"), (long)now.get("num"));
            }
            Calendar calendar = Calendar.getInstance();
            Map<String, Object> result = new HashMap<>();
            for (int i = 0; i < 7; i++) {
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - (i == 0 ? 0 : 1));
                Date today = calendar.getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String str = format.format(today);
                if (queryResult.get(str) == null) {
                    result.put(str, 0);
                } else {
                    result.put(str, queryResult.get(str));
                }
            }
            //将result按时间降序排序
            LinkedHashMap<String, Object> ascLinkedHashMap = result.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> newValue,
                            LinkedHashMap::new));
            redisUtil.set("root"+rootId+"NewComment", ascLinkedHashMap, 60);
            return ascLinkedHashMap;
        }
        else {
            Map<String, Object> result = (Map<String, Object>)redisUtil.get("root"+rootId+"NewComment");
            return result;
        }
    }
}
