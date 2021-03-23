package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.service.ElasticsearchService;
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

/**
 * @Description
 * @Author Mr.Dong <dongcf1997@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/22 22:09
 */
@Controller
public class SearchController implements CommunityConstant {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;


    @RequestMapping(path = "/search",method = RequestMethod.GET)
    public String search(String keyword, Page page, Model model){

        org.springframework.data.domain.Page<DiscussPost> discussPosts = elasticsearchService.searchDiscussPost(keyword, page.getCurrent()-1, page.getLimit());

        if(discussPosts==null){
            model.addAttribute("discussPosts",null);
            return "/site/search";
        }

        List<Map<String,Object>> list = new ArrayList<>();
        for (DiscussPost discussPost: discussPosts) {
            Map<String,Object> map = new HashMap<>();
            map.put("post",discussPost);
            //作者
            map.put("user",userService.findUserById(discussPost.getUserId()));
            //点赞数量
            map.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_POST,discussPost.getId()));

            list.add(map);
        }
        model.addAttribute("discussPosts",list);
        model.addAttribute("keyword",keyword);


        page.setPath("/search?keyword="+keyword);
        page.setRows(discussPosts == null ? 0: (int) discussPosts.getTotalElements());


        return "/site/search";
    }



}
