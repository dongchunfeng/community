package com.nowcoder.community;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
@Slf4j
public class LoggingTests {

    @Test
    public void test01(){
        log.debug("debug log");
        log.info("info log");
        log.warn("warn log");
        log.error("error log");
    }

}
