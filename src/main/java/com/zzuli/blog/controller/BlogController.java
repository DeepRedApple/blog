package com.zzuli.blog.controller;

import com.zzuli.blog.domain.User;
import com.zzuli.blog.domain.es.EsBlog;
import com.zzuli.blog.service.EsBlogService;
import com.zzuli.blog.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 博客管理控制页面
 * @author lizhenghao
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {

    private static final String HOT = "hot";
    private static final String NEW = "new";
	
	@Autowired
    private EsBlogService esBlogService;
	 
    @GetMapping
    public String listEsBlogs(
            @RequestParam(value="order",required=false,defaultValue="new") String order,
            @RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
            @RequestParam(value="async",required=false) boolean async,
            @RequestParam(value="pageIndex",required=false, defaultValue = "1") int pageIndex,
            @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
            Model model) {

        Page<EsBlog> blogPage = null;
        List<EsBlog> list = null;
        pageIndex = pageIndex - 1;
        // 系统初始化时，没有博客数据
        boolean isEmpty = true;
        try {
            // 最热查询
            if (order.equals(HOT)) {
                Sort sort = new Sort(Direction.DESC,"readSize","commentSize","voteSize","createTime"); 
                Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
                blogPage = esBlogService.listHotestEsBlogs(keyword, pageable);
                // 最新查询
            } else if (order.equals(NEW)) {
                Sort sort = new Sort(Direction.DESC,"createTime"); 
                Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
                blogPage = esBlogService.listNewestEsBlogs(keyword, pageable);
            }

            isEmpty = false;
        } catch (Exception e) {
            Pageable pageable = PageRequest.of(pageIndex, pageSize);
            blogPage = esBlogService.listEsBlogs(pageable);
        }

        // 当前所在页面数据列表
        list = blogPage.getContent();

        com.zzuli.blog.util.Page<EsBlog> page = new com.zzuli.blog.util.Page<EsBlog>(pageIndex, pageSize,
                (int) blogPage.getTotalElements(), blogPage.getContent());

        model.addAttribute("order", order);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);

        // 首次访问页面才加载
        if (!async && !isEmpty) {
            List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
            model.addAttribute("newest", newest);
            List<EsBlog> hotest = esBlogService.listTop5HotestEsBlogs();
            model.addAttribute("hotest", hotest);
            List<TagVO> tags = esBlogService.listTop30Tags();
            model.addAttribute("tags", tags);
            List<User> users = esBlogService.listTop12Users();
            model.addAttribute("users", users);
        }

        return (async==true?"/index :: #mainContainerRepleace":"/index");
    }
}
