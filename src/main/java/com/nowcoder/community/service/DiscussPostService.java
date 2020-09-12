package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPost(int userId, int offset, int limit){
        return discussPostMapper.selectAllDiscussPost(userId,offset,limit);
    }

    public int findDiscussPostCount(int userId){
        return discussPostMapper.selectCount(userId);
    }


}
