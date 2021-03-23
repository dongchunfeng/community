package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.event.EventProducer;
import com.nowcoder.community.service.FollowService;
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

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Mr.Dong <dongcf1997@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/17
 */
@Controller
public class FollowController implements CommunityConstant {

    @Autowired
    private FollowService followService;
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        //关注
        followService.follow(user.getId(), entityType, entityId);

        //触发关注事件
        Event event = new Event()
                .setTopic(TOPIC_FOLLOW)
                .setUserId(user.getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityId);
        eventProducer.fireEvent(event);

        return CommonUtils.getJSONString(0, "关注成功");
    }

    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        //取消关注
        followService.unfollow(user.getId(), entityType, entityId);

        return CommonUtils.getJSONString(0, "取消关注");
    }

    @RequestMapping(path = "/followees/{userId}", method = RequestMethod.GET)
    public String findFollowee(@PathVariable("userId") Integer userId, Page page, Model model) {
        User userById = userService.findUserById(userId);
        if (userById == null) {
            throw new RuntimeException("用户为空!");
        }
        model.addAttribute("user", userById);

        Long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        page.setLimit(5);
        page.setPath("/followees/" + userId);
        page.setRows(followeeCount.intValue());
        List<Map<String, Object>> followee = followService.findFollowee(userId, page.getOffset(), page.getLimit());
        if (followee != null) {
            for (Map<String, Object> map : followee) {
                User user = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(user.getId()));
            }
        }
        model.addAttribute("users", followee);
        return "/site/followee";
    }

    @RequestMapping(path = "/followers/{userId}", method = RequestMethod.GET)
    public String findFollower(@PathVariable("userId") Integer userId, Page page, Model model) {
        User userById = userService.findUserById(userId);
        if (userById == null) {
            throw new RuntimeException("用户为空!");
        }
        model.addAttribute("user", userById);
        Long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows(followerCount.intValue());
        List<Map<String, Object>> follower = followService.findFollowers(userId, page.getOffset(), page.getLimit());
        if (follower != null) {
            for (Map<String, Object> map : follower) {
                User user = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(user.getId()));
            }
        }
        model.addAttribute("users", follower);
        return "/site/follower";
    }

    /**
     * 查查询当前用户是否关注该实体
     *
     * @param userId
     * @return
     */
    private boolean hasFollowed(int userId) {
        if (hostHolder.getUser() == null) {
            return false;
        }
        return followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
    }


}
