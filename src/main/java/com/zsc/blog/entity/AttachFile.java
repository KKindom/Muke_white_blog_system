package com.zsc.blog.entity;

import lombok.Data;

import java.util.Date;

/**
 * 上传文件的相关信息
 * @author
 */
@Data
public class AttachFile {
    /**
     * 保存后的文件名
     */
    String filename;
    //文件虚拟路径
    String Virtual_path;
    //文件物理路径
    String Physical_path;
    /**
     *  文件大小
     */
    long fileSize;
    /**
     * 上传时间
     */
    Date uploadTime;
    /**
     * 文件类型
     */
    String type;
}
