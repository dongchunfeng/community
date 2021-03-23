package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.*;
import com.nowcoder.community.util.CommonUtils;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${community.path.domain}")
    private String domain;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private FollowService followService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private DiscussPostService discussPostService;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/myreply/{userId}", method = RequestMethod.GET)
    public String getMyReplyPage(@PathVariable("userId")int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        page.setLimit(5);
        page.setPath("/myreply"+userId);
        page.setRows(commentService.findReplyByCount(CommunityConstant.ENTITY_TYPE_POST,userId));
        List<Comment> list= commentService.findReplyByUserId(CommunityConstant.ENTITY_TYPE_POST,userId,page.getOffset(),page.getLimit());
        List<Map<String, Object>> myReplyList = new ArrayList<>();
        //查询帖子标题
        for (Comment comment : list){
            Map<String, Object> map = new HashMap<>();
            DiscussPost post = discussPostService.getDiscussPostById(comment.getEntityId());
            map.put("post",post);
            map.put("comment",comment);
            myReplyList.add(map);
        }
        model.addAttribute("myreplys",myReplyList);
        model.addAttribute("user",user);
        return "/site/my-reply";
    }


    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "你还没有选择图片呢");
            return "/site/setting";
        }

        String filename = headerImage.getOriginalFilename();//dd.jpg
        //获取后缀名
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (suffix == null) {
            model.addAttribute("error", "文件格式不正确");
            return "/site/setting";
        }

        filename = CommonUtils.generateUUID() + suffix;
        File dest = new File(uploadPath + "/" + filename);
        try {
            //存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            log.error("上传文件失败:" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器异常:" + e);
        }

        //更新当前用户的头像地址
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + filename;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    /**
     * 获取头像
     * @param fileName
     * @param response
     */
    @RequestMapping(path = "/header/{fileName}",method = RequestMethod.GET)
    public void getHeaderUrl(@PathVariable String fileName, HttpServletResponse response){
        fileName = uploadPath + "/" + fileName;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        response.setContentType("image/"+suffix);
        try (
                OutputStream os = response.getOutputStream();
                FileInputStream fis = new FileInputStream(fileName);
                ){
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = fis.read(buffer))!=-1){
                os.write(buffer,0,len);
            }
        } catch (IOException e) {
            log.error("获取图片异常:"+e.getMessage());
            throw new RuntimeException("获取图片失败，服务器异常:" + e);
        }

    }


    @RequestMapping(path = "/profile/{userId}",method = RequestMethod.GET)
    public String profile(@PathVariable("userId") int userId,Model model){
        User user = userService.findUserById(userId);
        if(user==null){
            throw new RuntimeException("用户为空");
        }
        //点赞数量
        int userLikeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("user",user);
        model.addAttribute("likeCount",userLikeCount);
        //关注者数量
        long followeeCount = followService.findFolloweeCount(userId, CommunityConstant.ENTITY_TYPE_USER);
        //粉丝数量
        long followerCount = followService.findFollowerCount(CommunityConstant.ENTITY_TYPE_USER, userId);
        model.addAttribute("followeeCount",followeeCount);
        model.addAttribute("followerCount",followerCount);
        //是否已关注
        boolean hasFollowed = false;
        if(hostHolder.getUser()!=null){
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), CommunityConstant.ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed",hasFollowed);
        return "/site/profile";
    }



}
