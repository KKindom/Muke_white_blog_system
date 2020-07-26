package com.zsc.blog.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
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
@TableName("t_comment")
public class TComment extends Model<TComment> {

    private static final long serialVersionUID = 1L;

    /**
     * 评论id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 关联的文章id
     */
    @TableField("article_id")
    private Integer articleId;
    /**
     * 评论时间
     */
    private Date created;
    /**
     * 评论用户登录的ip地址
     */
    private String ip;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论状态
     */
    private String status;
    /**
     * 评论用户用户名
     */
    private String author;


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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TComment{" +
        "id=" + id +
        ", articleId=" + articleId +
        ", created=" + created +
        ", ip=" + ip +
        ", content=" + content +
        ", status=" + status +
        ", author=" + author +
        "}";
    }
}
