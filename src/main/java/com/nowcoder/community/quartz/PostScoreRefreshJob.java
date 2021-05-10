package com.nowcoder.community.quartz;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.ElasticsearchService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description
 * @Author Mr.Dong <dongcf1997@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/26 11:41
 */
@Slf4j
public class PostScoreRefreshJob implements Job, CommunityConstant {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private ElasticsearchService elasticsearchService;

    private static final Date epoch;

    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-08-01 00:00:00");
        } catch (Exception e) {
            throw new RuntimeException("初始化时间出错", e);
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String rediskey = RedisKeyUtil.getPostScoreKey();

        BoundSetOperations operations = redisTemplate.boundSetOps(rediskey);

        if (operations.size() == 0) {
            log.info("[任务取消] 没有需要刷新的帖子");
        }

        log.info("[任务开始] 正在刷新帖子分数:" + operations.size());
        while (operations.size() > 0) {
            this.refresh((Integer) operations.pop());
        }
        log.info("[任务结束] 刷新帖子分数完毕:");
    }

    public void refresh(int postId) {
        DiscussPost post = discussPostService.getDiscussPostById(postId);
        if (post == null) {
            log.info("该帖子不存在: id = " + postId);
            return;
        }

        //是否置顶
        boolean wonderful = post.getStatus() == 1;
        //点赞数
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, postId);
        //评论数
        int commentCount = post.getCommentCount();

        //计算分数
        double w = (wonderful ? 75 : 0) + likeCount * 2 + commentCount * 10;

        double score = Math.log10(Math.max(w, 1))
                + (post.getCreateTime().getTime() - epoch.getTime()) / (1000 * 3600 * 24);
        //更新帖子数据
        discussPostService.updateScore(postId, score);
        post.setScore(score);

        elasticsearchService.saveDiscussPost(post);


    }

}
