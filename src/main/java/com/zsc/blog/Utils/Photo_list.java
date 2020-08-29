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
       photo_list.add("https://c-ssl.duitang.com/uploads/item/201503/27/20150327221603_QcUzN.jpeg");
       photo_list.add("https://c-ssl.duitang.com/uploads/item/201806/05/20180605193845_kmhss.jpeg");
       photo_list.add("https://c-ssl.duitang.com/uploads/item/201707/13/20170713172041_UhiJw.jpeg") ;
    photo_list.add("https://c-ssl.duitang.com/uploads/item/201509/20/20150920105348_38Ewf.jpeg");
        photo_list.add("https://c-ssl.duitang.com/uploads/item/201707/09/20170709134436_Nvdwk.jpeg");
        photo_list.add("https://c-ssl.duitang.com/uploads/item/201711/12/20171112094922_nViQB.thumb.700_0.jpeg");
        photo_list.add("https://c-ssl.duitang.com/uploads/item/201707/27/20170727234837_efxti.thumb.700_0.jpeg");
        photo_list.add("https://c-ssl.duitang.com/uploads/item/201512/13/20151213113905_TsAvf.thumb.700_0.jpeg");
        photo_list.add("https://c-ssl.duitang.com/uploads/item/201711/04/20171104144212_jnrKV.thumb.700_0.jpeg");
        photo_list.add("https://c-ssl.duitang.com/uploads/item/201709/28/20170928183516_TKzGV.thumb.700_0.jpeg");
        photo_list.add("https://c-ssl.duitang.com/uploads/item/201709/23/20170923185515_L5kzC.thumb.700_0.jpeg");
    }
    public String find_photo()
    {
        int vcode=(int)((Math.random()*10+1));
        return photo_list.get(vcode);
    }
}
