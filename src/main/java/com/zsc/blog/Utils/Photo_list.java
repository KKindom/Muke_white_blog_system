package com.zsc.blog.Utils;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: blog_system_f
 * @description: 存网络头像地址
 * @author: Mr.Wang
 * @create: 2020-08-29 21:39
 **/
@Repository
public class Photo_list
{
    private List<String> photo_list=new ArrayList<>();

    public Photo_list() {

        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/1.jpeg");
        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/2.jpeg");
        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/3.jpeg");
        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/4.jpeg");
        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/5.jpeg");
        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/6.jpeg");
        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/7.jpeg");
        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/8.jpeg");
        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/9.jpeg");
        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/10.jpeg");
        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/11.jpeg");
        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/12.jpeg");
        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/13.jpeg");
        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/14.jpeg");
        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/15.jpeg");
        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/16.jpeg");
        photo_list.add("http://60.205.178.189:8080/uploadfile/user_profile_photo/17.jpeg");


    }
    public String find_photo()
    {
        int vcode=(int)((Math.random()*10+100)%18);
        return photo_list.get(vcode);
    }
}
