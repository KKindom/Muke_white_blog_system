package com.zsc.blog.Utils;

import com.zsc.blog.entity.AttachFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 功能：用于管理上传的文件
 *
 * @author
 */
@Component
public class FileUploadUtils {
    /**
     * @param uploadDir 文件保存的路径
     * @param file 上传的文件
     * @return AttachFile文件对象
     * @throws Exception
     */
    // 文件的真实路径
    @Value("${file.uploadFolder}")
    private String realBasePath;
    @Value("${file.accessPath}")
    private String accessPath;

    public  AttachFile upload(MultipartFile file) throws IOException {
        //获得后缀
        String fileName = file.getOriginalFilename();
        String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(fileSuffix);
        // 文件唯一的名字
        fileName = UUID.randomUUID().toString()  +fileSuffix;
        Date todayDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(todayDate);
        // 域名访问的相对路径（通过浏览器访问的链接-虚拟路径）
        String saveToPath = accessPath + today + "/";
        // 真实路径，实际储存的路径
        String realPath = realBasePath + today + "/";
        // 储存文件的物理路径，使用本地路径储存
        String filepath = realPath;
        System.out.println("上传图片名为：" + fileName+"--虚拟文件路径为：" + saveToPath +"--物理文件路径为：" + realPath);
        System.out.println(saveToPath+fileName);

        //获取文件保存路径
    File dir=new File(filepath);
    if(!dir.exists())
    {
        dir.mkdirs();
    }
    AttachFile attachFile=new AttachFile();
    if(!file.isEmpty())
    {
            File newfile = new File(dir, fileName);
            file.transferTo(newfile);
        attachFile.setOriginalFilename(realPath);
        attachFile.setFilename(fileName);
        attachFile.setType(file.getContentType());
        attachFile.setFileSize(file.getSize());
        attachFile.setFileUUID(UUID.randomUUID().toString());
        attachFile.setUploadTime(new Date());
        System.out.println("文件"+attachFile+"上传成功！");
    }
    return attachFile;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath)
    {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists())
        {
            file.delete();
            flag = true;
        }
        return flag;
    }
}
