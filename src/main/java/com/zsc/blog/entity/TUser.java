package com.zsc.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author mff
 * @since 2020-07-26
 */
public class TUser extends Model<TUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码加密后的
     */
    private String password;

    /**
     * 用户邮箱(用于找回密码)
     */
    private String email;

    /**
     * 用户权限  为admin和client
     */
    private String permisson;

    /**
     * 用户头像 有一个默认的
     */
    private String profilephoto;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPermisson() {
        return permisson;
    }

    public void setPermisson(String permisson) {
        this.permisson = permisson;
    }

    public String getProfilephoto() {
        return profilephoto;
    }

    public void setProfilephoto(String profilephoto) {
        this.profilephoto = profilephoto;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TUser{" +
        "id=" + id +
        ", username=" + username +
        ", password=" + password +
        ", email=" + email +
        ", permisson=" + permisson +
        ", profilephoto=" + profilephoto +
        "}";
    }
}
