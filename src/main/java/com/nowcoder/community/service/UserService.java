package com.nowcoder.community.service;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommonUtils;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.IMailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IMailService iMailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String path;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        if (StringUtils.isBlank(user.getUsername())) {
            map.put("userNameMsg", "用户账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "用户密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "用户邮箱不能为空");
            return map;
        }

        //验证账号
        User u = userMapper.selectByUserName(user.getUsername());
        if (u != null) {
            map.put("userNameMsg", "账号已存在");
            return map;
        }

        User user1 = userMapper.selectByEmail(user.getEmail());
        if (user1 != null) {
            map.put("emailMsg", "邮箱已存在");
            return map;
        }


        //设置空值 并添加数据库
        user.setType(0);
        user.setStatus(0);
        user.setSalt(CommonUtils.generateUUID().substring(0, 5));
        user.setPassword(CommonUtils.md5(user.getPassword() + user.getSalt()));
        user.setCreateTime(new Date());
        user.setActivationCode(CommonUtils.generateUUID());
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png", new Random().nextInt(100)));
        userMapper.insertUser(user);

        //发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        //"http://localhost:8080/community/activation/101/code";
        String url = domain + path + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        iMailService.sendHtmlMail(user.getEmail(), "激活账号", content);

        return map;
    }

    public int activationUser(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAIL;
        }

    }


}
