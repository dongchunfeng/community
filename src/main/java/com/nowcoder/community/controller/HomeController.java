package com.nowcoder.community.controller;


import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){
        page.setRows(discussPostService.findDiscussPostCount(0));
        page.setPath("/index");

        List<DiscussPost> discussPost = discussPostService.findDiscussPost(0, page.getOffset(), page.getLimit());
        List<Map<String,Object>> disPostList = new ArrayList<>();
        for (DiscussPost post:discussPost) {
            Map<String,Object> map = new HashMap<>();
            map.put("post",post);
            map.put("user",userService.findUserById(post.getUserId()));

            long entityLikeCount = likeService.findEntityLikeCount(CommunityConstant.ENTITY_TYPE_POST, post.getId());
            map.put("likeCount",entityLikeCount);

            disPostList.add(map);
        }
        model.addAttribute("discussPosts",disPostList);
        return "/index";
    }

    @RequestMapping(path = "/error",method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }
}
