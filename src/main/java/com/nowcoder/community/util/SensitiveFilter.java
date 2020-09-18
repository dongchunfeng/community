package com.nowcoder.community.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SensitiveFilter {

    //替换符
    private static final String REPLACEMENT = "***";

    //根节点
    private TireNode rootNode = new TireNode();

    @PostConstruct
    public void init(){
        this.getClass().getClassLoader().getResourceAsStream("");
    }

    //前缀树
    public class TireNode {

        //关键词结束标识
        private boolean isKeyWordEnd = false;

        //子节点
        private Map<Character, TireNode> subNodes = new HashMap<>();

        public void addSubNode(Character c, TireNode t) {
            subNodes.put(c, t);
        }

        public TireNode getSubNode(Character c) {
            return subNodes.get(c);
        }

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }
    }


}
