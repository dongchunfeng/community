package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommonUtils;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @RequestMapping(path = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title,String content){
        User user = hostHolder.getUser();
        if(user==null){
            return CommonUtils.getJSONString(403,"你还没登录哦!");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPost.setUserId(user.getId());
        discussPostService.addDiscussPost(discussPost);
        return CommonUtils.getJSONString(0,"帖子发布成功!");
    }

    @RequestMapping(path="/detail/{discussPostId}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){
        //帖子详情
        DiscussPost post = discussPostService.getDiscussPostById(discussPostId);
        model.addAttribute("post",post);
        //用户
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);

        page.setLimit(5);
        page.setPath("/discuss/detail/"+discussPostId);
        page.setRows(post.getCommentCount());

        //评论     回复评论
        List<Comment> commentList =commentService.selectCommentByEntity(ENTITY_TYPE_POST,post.getId(),page.getOffset(),page.getLimit());
        //评论列表
        List<Map<String,Object>> commentVOList = new ArrayList<>();
        if(commentList!=null){
            for (Comment comment: commentList) {
                Map<String,Object> commentVO = new HashMap<>();
                commentVO.put("comment",comment);
                commentVO.put("user",userService.findUserById(comment.getUserId()));

                //回复评论列表
                List<Comment> replyComment = commentService.selectCommentByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String,Object>> replyVOList = new ArrayList<>();
                if(replyComment!=null){
                    for (Comment reply : replyComment){
                        Map<String,Object> replyVO = new HashMap<>();
                        replyVO.put("reply",reply);
                        replyVO.put("user",userService.findUserById(reply.getUserId()));
                        //回复用户的目标
                        User target = reply.getTargetId()==0?null:userService.findUserById(reply.getTargetId());
                        replyVO.put("target",target);

                        replyVOList.add(replyVO);
                    }
                }
                commentVO.put("replys",replyVOList);

                //回复次数
                int replyCounts = commentService.findCommentCount(ENTITY_TYPE_COMMENT,comment.getId());
                commentVO.put("replyCount",replyCounts);

                commentVOList.add(commentVO);

            }
        }
        model.addAttribute("comments",commentVOList);

        return "/site/discuss-detail";
    }


}
