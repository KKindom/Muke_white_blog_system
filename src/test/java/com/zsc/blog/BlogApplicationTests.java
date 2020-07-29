package com.zsc.blog;

import com.zsc.blog.entity.Page_article;
import com.zsc.blog.mapper.TArticleMapper;
import com.zsc.blog.service.ITArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ClassUtils;

import java.util.List;

@SpringBootTest
class BlogApplicationTests {
    @Autowired
    ITArticleService itArticleService;
    @Autowired
    TArticleMapper tArticleMapper;
    @Test
    void contextLoads() {
    }
    @Test
    void savefile()
    {
      List<Page_article> page_articles= itArticleService.select_page(3,4,9);
        System.out.println(page_articles);
    }

}
