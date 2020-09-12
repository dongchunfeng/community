package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "site/register";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "site/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);

        if (map.isEmpty() || map == null) {
            model.addAttribute("msg", "注册成功，我们已向你发送一条激活邮箱，请尽快激活");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("userNameMsg", map.get("userNameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    @RequestMapping(value = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String register(Model model,@PathVariable int userId,@PathVariable String code) {

        int result = userService.activationUser(userId, code);

        if(result==ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功,你的账号可以正常使用了.");
            model.addAttribute("target","/login");
        }else if(result==ACTIVATION_REPEAT){
            model.addAttribute("msg","无效操作,该账号已激活.");
            model.addAttribute("target","/index");
        }else{
            model.addAttribute("msg","激活失败,你提供的激活码不正确.");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }


}
