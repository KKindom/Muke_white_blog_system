package com.zsc.blog.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author mff
 * @since 2020-07-25
 */
@TableName("t_statistic")
public class TStatistic extends Model<TStatistic> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 关联的文章id
     */
    @TableField("article_id")
    private Integer articleId;
    /**
     * 文章点击总量
     */
    private Integer hits;
    /**
     * 文章评论量
     */
    @TableField("comments_num")
    private Integer commentsNum;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Integer getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(Integer commentsNum) {
        this.commentsNum = commentsNum;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TStatistic{" +
        "id=" + id +
        ", articleId=" + articleId +
        ", hits=" + hits +
        ", commentsNum=" + commentsNum +
        "}";
    }
}
