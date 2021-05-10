package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.elasticsearch.DiscusspostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description
 * @Author Mr.Dong <dongcf1997@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/22 13:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ElasticsearchTests {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private DiscusspostRepository discusspostRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void test01(){
        discusspostRepository.saveAll(discussPostMapper.selectAllDiscussPost(101, 0, 100));
        discusspostRepository.saveAll(discussPostMapper.selectAllDiscussPost(102, 0, 100));
        discusspostRepository.saveAll(discussPostMapper.selectAllDiscussPost(103, 0, 100));
        discusspostRepository.saveAll(discussPostMapper.selectAllDiscussPost(111, 0, 100));
        discusspostRepository.saveAll(discussPostMapper.selectAllDiscussPost(112, 0, 100));
        discusspostRepository.saveAll(discussPostMapper.selectAllDiscussPost(131, 0, 100));
        discusspostRepository.saveAll(discussPostMapper.selectAllDiscussPost(132, 0, 100));
        discusspostRepository.saveAll(discussPostMapper.selectAllDiscussPost(133, 0, 100));
        discusspostRepository.saveAll(discussPostMapper.selectAllDiscussPost(134, 0, 100));
    }

    @Test
    public void testDel(){

        discusspostRepository.deleteAll();

    }


}
