package com.nowcoder.community.service;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
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

import java.util.*;

/**
 * @author MrDong
 */
@Service
public class UserService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IMailService iMailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

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

    public Map<String,Object> login(String username,String password,boolean rememberme){
        Map<String,Object> map = new HashMap<>();

        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return map;
        }

        User user = userMapper.selectByUserName(username);
        if(user == null){
            map.put("usernameMsg","该账号不存在");
            return map;
        }

        password = CommonUtils.md5(password+user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg","密码不正确");
            return map;
        }

        //生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommonUtils.generateUUID());
        //有效
        loginTicket.setStatus(0);
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        // 设置起时间
        cal.setTime(date);
        if(rememberme){
            // 增加一年
            cal.add(Calendar.YEAR, 1);
            loginTicket.setExpired(cal.getTime());
        }
        // 增加一天
        cal.add(Calendar.HOUR, 1);
        loginTicket.setExpired(cal.getTime());
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    public void layout(String ticket){
        //失效
        loginTicketMapper.updateStatus(ticket,1);
    }

    public LoginTicket getByTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);
    }

    public void updateHeader(int id,String url){
        userMapper.updateHeader(id,url);
    }

    public User findUserByName(String username){
        return userMapper.selectByUserName(username);
    };

}
