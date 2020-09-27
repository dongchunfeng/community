package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testSelectId() {
        User user = userMapper.selectById(1);
        System.out.println(user);
    }

    @Test
    public void testSelectName() {
        User guanyu = userMapper.selectByUserName("guanyu");
        System.out.println(guanyu);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        UUID uuid = UUID.randomUUID();
        user.setActivationCode(uuid.toString());
        user.setCreateTime(new Date());
        user.setEmail("1013084647@qq.com");
        user.setPassword("123456");
        user.setSalt("1111");
        user.setType(0);
        user.setUsername("MrDong");
        int i = userMapper.insertUser(user);
        System.out.println(i);
    }

    @Test
    public void testSelectDiscussPost() {
        List<DiscussPost> discussPosts = discussPostMapper.selectAllDiscussPost(0, 0, 10);
        for (DiscussPost dis : discussPosts
        ) {
            System.out.println(dis);
        }

        int i = discussPostMapper.selectCount(0);
        System.out.println(i);

    }

    @Test
    public void testLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60));
        loginTicket.setTicket(CommonUtils.generateUUID());
        loginTicket.setUserId(101);
        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testLoginTicket1() {

        LoginTicket loginTicket = loginTicketMapper.selectByTicket("aa");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("aa",1);

    }

    @Test
    public void testMessage(){

        List<Message> messages = messageMapper.selectConversation(111, 0, 20);
        for (Message message:messages){
            System.out.println(message);
        }

        int i = messageMapper.selectConversationCount(111);
        System.out.println(i);

        List<Message> messages1 = messageMapper.selectLetters("111_112", 0, 20);
        for (Message message:messages1){
            System.out.println(message);
        }

        int i1 = messageMapper.selectLettersCount("111_112");
        System.out.println(i1);

        int i2 = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(i2);

    }

}
